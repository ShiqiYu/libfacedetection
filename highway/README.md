# hw experiment

Independent Highway-based performance experiment for libfacedetection.

Configure with the prebuilt Highway 1.3.0 package:

```powershell
cmake -S highway -B build-hw -Dhwy_DIR=<path-to-highway>/lib/cmake/hwy
cmake --build build-hw --config Release
.\build-hw\Release\fdt_hw_tests.exe
.\build-hw\Release\fdt_hw_benchmark.exe
```

For image benchmark targets, also pass `OpenCV_DIR`:

```powershell
cmake -S highway -B build-hw `
  -Dhwy_DIR=<path-to-highway>/lib/cmake/hwy `
  -DOpenCV_DIR=<path-to-opencv>/staticlib
cmake --build build-hw --config Release --target fdt_hw_image_benchmark
.\build-hw\Release\fdt_hw_image_benchmark.exe images\cnnresult.png
```
