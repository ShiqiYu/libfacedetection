#include "facedetect_hw.h"
#include "facedetectcnn.h"

#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/imgproc.hpp>

#include <algorithm>
#include <chrono>
#include <cstdio>
#include <cstdlib>
#include <thread>
#include <vector>

namespace {

struct SizeCase {
    int width;
    int height;
};

struct Stats {
    double ms;
    double fps;
    int faces;
};

struct Detector {
    const char* name;
    size_t buffer_size;
    int* (*fn)(unsigned char*, unsigned char*, int, int, int);
};

int ThreadCountFromArgs(int argc, char** argv) {
    if (argc > 3) {
        const int value = std::atoi(argv[3]);
        if (value > 0) {
            return value;
        }
    }
    const unsigned int detected = std::thread::hardware_concurrency();
    return detected == 0 ? 1 : static_cast<int>(detected);
}

int IterationsFromArgs(int argc, char** argv) {
    if (argc > 2) {
        const int value = std::atoi(argv[2]);
        if (value > 0) {
            return value;
        }
    }
    return 128;
}

Stats MeasureSingle(const Detector& detector,
                    const cv::Mat& image,
                    int iterations) {
    std::vector<unsigned char> buffer(detector.buffer_size);
    for (int i = 0; i < 5; ++i) {
        detector.fn(buffer.data(), image.data, image.cols, image.rows,
                    static_cast<int>(image.step));
    }

    const auto start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < iterations; ++i) {
        detector.fn(buffer.data(), image.data, image.cols, image.rows,
                    static_cast<int>(image.step));
    }
    const auto stop = std::chrono::high_resolution_clock::now();
    const double total_ms =
        std::chrono::duration<double, std::milli>(stop - start).count();
    const double ms = total_ms / static_cast<double>(iterations);
    const int faces = *reinterpret_cast<int*>(buffer.data());
    return Stats{ms, 1000.0 / ms, faces};
}

Stats MeasureExternalThreads(const Detector& detector,
                             const cv::Mat& image,
                             int iterations,
                             int thread_count) {
    const int total_iterations = std::max(iterations, thread_count);
    std::vector<std::vector<unsigned char> > buffers(
        static_cast<size_t>(thread_count),
        std::vector<unsigned char>(detector.buffer_size));

    for (int t = 0; t < thread_count; ++t) {
        detector.fn(buffers[static_cast<size_t>(t)].data(), image.data,
                    image.cols, image.rows, static_cast<int>(image.step));
    }

    const auto start = std::chrono::high_resolution_clock::now();
    std::vector<std::thread> threads;
    threads.reserve(static_cast<size_t>(thread_count));
    for (int t = 0; t < thread_count; ++t) {
        threads.push_back(std::thread([&, t] {
            const int begin = total_iterations * t / thread_count;
            const int end = total_iterations * (t + 1) / thread_count;
            unsigned char* buffer = buffers[static_cast<size_t>(t)].data();
            for (int i = begin; i < end; ++i) {
                detector.fn(buffer, image.data, image.cols, image.rows,
                            static_cast<int>(image.step));
            }
        }));
    }
    for (size_t i = 0; i < threads.size(); ++i) {
        threads[i].join();
    }
    const auto stop = std::chrono::high_resolution_clock::now();
    const double total_ms =
        std::chrono::duration<double, std::milli>(stop - start).count();
    const double ms = total_ms / static_cast<double>(total_iterations);
    const int faces = *reinterpret_cast<int*>(buffers[0].data());
    return Stats{ms, 1000.0 / ms, faces};
}

}  // namespace

int main(int argc, char** argv) {
    const char* image_path = argc > 1 ? argv[1] : "images/cnnresult.png";
    const int iterations = IterationsFromArgs(argc, argv);
    const int thread_count = ThreadCountFromArgs(argc, argv);
    cv::Mat source = cv::imread(image_path, cv::IMREAD_COLOR);
    if (source.empty()) {
        std::fprintf(stderr, "failed to load image: %s\n", image_path);
        return 1;
    }

    const SizeCase cases[] = {
        {640, 480},
        {320, 240},
        {160, 120},
        {128, 96},
    };
    const Detector detectors[] = {
        {"origin", FACEDETECTION_RESULT_BUFFER_SIZE, facedetect_cnn},
        {"highway", FACEDETECTION_HW_RESULT_BUFFER_SIZE, facedetect_hw_cnn},
    };

    std::printf("image=%s iterations=%d external_threads=%d\n", image_path,
                iterations, thread_count);
    for (size_t d = 0; d < sizeof(detectors) / sizeof(detectors[0]); ++d) {
        std::printf("\n%s\n", detectors[d].name);
        std::printf("| Input | Single ms | Single FPS | External MT ms | External MT FPS | Faces |\n");
        std::printf("|-------|-----------|------------|----------------|-----------------|-------|\n");
        for (size_t i = 0; i < sizeof(cases) / sizeof(cases[0]); ++i) {
            cv::Mat resized;
            cv::resize(source, resized,
                       cv::Size(cases[i].width, cases[i].height));
            const Stats single = MeasureSingle(detectors[d], resized, iterations);
            const Stats multi = MeasureExternalThreads(
                detectors[d], resized, iterations, thread_count);
            std::printf("| %dx%d | %.2f | %.2f | %.2f | %.2f | %d |\n",
                        cases[i].width, cases[i].height, single.ms,
                        single.fps, multi.ms, multi.fps, single.faces);
        }
    }

    return 0;
}
