# hw experiment

Independent Highway-based performance experiment for libfacedetection.

Configure with the prebuilt Highway 1.3.0 package:

```powershell
cmake -S highway -B build-hw -DFDT_HW_HIGHWAY_ROOT=E:/projects/thirdParty/lib/highway-1.3.0
cmake --build build-hw --config Release
.\build-hw\Release\fdt_hw_tests.exe
.\build-hw\Release\fdt_hw_benchmark.exe
```

The current slice builds primitive and small-kernel Highway implementations plus scalar reference code. The full `facedetect_hw_cnn` network entry point will be added after this feedback loop is stable.
