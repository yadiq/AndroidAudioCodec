
# CMAKE的最低版本
cmake_minimum_required(VERSION 3.4.1)

# 获取目录下所有CPP的文件
file(GLOB_RECURSE SOURCE_CPP_FILES_OF_PARENT "${CMAKE_CURRENT_SOURCE_DIR}/*.c")
file(GLOB_RECURSE SOURCE_CPP_FILES_OF_UTILS "${CMAKE_CURRENT_SOURCE_DIR}/codec/*.c")

# 添加动态库，指定库名称、类型、相对路径
add_library(
        audio_codec
        SHARED
        ${SOURCE_CPP_FILES_OF_PARENT}
        ${SOURCE_CPP_FILES_OF_UTILS}
)

# 链接库
target_link_libraries(
        audio_codec
        android
        log
)