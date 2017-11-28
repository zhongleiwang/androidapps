#include <stdio.h>
#include <android/log.h>

int main() {
  int i = 99;
  printf("hello ??? %d !!!\n", i);
  __android_log_print (ANDROID_LOG_FATAL, "???", "%d", i);
  return 0;
}
