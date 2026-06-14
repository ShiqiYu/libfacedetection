#pragma once

#include "hw_kernels.h"

#include <vector>

namespace fdt {
namespace hw {

class HwBlob {
public:
    HwBlob();
    HwBlob(int rows, int cols, int channels);

    void Resize(int rows, int cols, int channels);
    void ResizeForOverwrite(int rows, int cols, int channels);
    void Clear();

    bool Empty() const;

    BlobView View();
    ConstBlobView View() const;

    float* Ptr(int row, int col);
    const float* Ptr(int row, int col) const;

    int rows() const { return rows_; }
    int cols() const { return cols_; }
    int channels() const { return channels_; }
    int channel_step() const { return channel_step_; }
    size_t size() const { return data_.size(); }

    std::vector<float>& data() { return data_; }
    const std::vector<float>& data() const { return data_; }

private:
    int rows_;
    int cols_;
    int channels_;
    int channel_step_;
    std::vector<float> data_;
};

}  // namespace hw
}  // namespace fdt
