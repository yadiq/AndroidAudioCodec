LOCAL_PATH := $(call my-dir) #源文件在开发树中的位置

include $(CLEAR_VARS) #清除许多变量

LOCAL_MODULE := AudioCodec #模块名称

LOCAL_SRC_FILES := main.c #列举源文件
LOCAL_SRC_FILES += g72x.c
LOCAL_SRC_FILES += g711.c
LOCAL_SRC_FILES += g721.c
LOCAL_SRC_FILES += g723_16.c
LOCAL_SRC_FILES += g723_24.c
LOCAL_SRC_FILES += g723_40.c

# for logging
LOCAL_LDLIBS    := -llog
# for native windows
# for native asset manager
LOCAL_LDLIBS    += -landroid

#not allowed with 'C++'
# LOCAL_CFLAGS    += -std=c99
#not allowed with 'C'
# LOCAL_CFLAGS    += -std=c++11 -Wall -UNDEBUG

include $(BUILD_SHARED_LIBRARY) #连接到一起，共享库 (.so) 静态库 (.a)




