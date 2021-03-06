#include <jni.h>

#include "exchange/app/src/bazel/jni_dep.h"

const char* hello = "Hello JNI";

extern "C" JNIEXPORT jstring JNICALL
Java_bazel_Jni_hello(JNIEnv *env, jclass clazz) {
  return NewStringLatin1(env, hello);
}
