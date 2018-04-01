#include <android/log.h> 
#include <stdio.h> 

extern int log();

int main() { 
  printf("hello world !!!\n");
  __android_log_print (ANDROID_LOG_FATAL, "???", "hello world!");

  log();

  return 0;
} 
