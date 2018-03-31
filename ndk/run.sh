#!/bin/bash

bazel build --crosstool_top=//external:android/crosstool --host_crosstool_top=@bazel_tools//tools/cpp:toolchain --config=android --cpu=arm64-v8a //ndk:main
adb push bazel-bin/ndk/main /data/local/tmp/main
adb shell /data/local/tmp/main
