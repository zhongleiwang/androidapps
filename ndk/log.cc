#include <android/log.h> 
#include <stdio.h> 

int log() { 
  printf("log ... ...\n"); 
  __android_log_print (ANDROID_LOG_FATAL, "???", "log ..."); 
  return 0; 
} 
