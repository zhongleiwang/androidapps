### Build

bazel build --config=android_arm64 --cpu=arm64-v8a camera:camera

.bazelrc file

```shell
build --javacopt="-source 8"
build --javacopt="-target 8"

# http://ci.tensorflow.org/view/Nightly/job/nightly-android/ws/.tf_configure.bazelrc/*view*/
build --action_env PYTHON_BIN_PATH="/usr/bin/python"
build --action_env PYTHON_LIB_PATH="/usr/local/lib/python2.7/dist-packages"
build --force_python=py2
build --host_force_python=py2
build --python_path="/usr/bin/python"
build --define with_jemalloc=true
build --define with_gcp_support=true
build --define with_hdfs_support=true
build --define with_s3_support=true
build:xla --define with_xla_support=true
build:gdr --define with_gdr_support=true
build:verbs --define with_verbs_support=true
build --action_env TF_NEED_OPENCL_SYCL="0"
build --action_env TF_NEED_CUDA="0"
build --define grpc_no_ares=true
build:opt --copt=-march=native
build:opt --host_copt=-march=native
build:opt --define with_default_optimizations=true
build --copt=-DGEMMLOWP_ALLOW_SLOW_SCALAR_FALLBACK
build --host_copt=-DGEMMLOWP_ALLOW_SLOW_SCALAR_FALLBACK
build:mkl --define using_mkl=true
build:mkl -c opt
build:monolithic --define framework_shared_object=false
build --define framework_shared_object=true
build:android --crosstool_top=//external:android/crosstool
build:android --host_crosstool_top=@bazel_tools//tools/cpp:toolchain
build:android_arm --config=android
build:android_arm --cpu=armeabi-v7a
build:android_arm64 --config=android
build:android_arm64 --cpu=arm64-v8a
```

### Run

```shell
$ adb push bazel-bin/camera/camera /data/local/tmp/
$ adb shell /data/local/tmp/camera
```
