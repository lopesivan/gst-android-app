cmake_minimum_required(VERSION 3.22.1)

project(gst-android-app LANGUAGES C CXX)

if(NOT DEFINED GSTREAMER_ROOT_ANDROID)
    message(FATAL_ERROR "GSTREAMER_ROOT_ANDROID não foi definido")
endif()

if(NOT ANDROID_ABI STREQUAL "arm64-v8a")
    message(FATAL_ERROR "ABI não suportada: ${ANDROID_ABI}; use arm64-v8a")
endif()

set(GSTREAMER_ROOT "${GSTREAMER_ROOT_ANDROID}/arm64")
list(APPEND CMAKE_MODULE_PATH "${GSTREAMER_ROOT}/share/cmake")

find_package(GStreamerMobile REQUIRED COMPONENTS
    coreelements
    audiotestsrc
    audioconvert
    audioresample
    opensles
)

find_library(LOG_LIBRARY log REQUIRED)

add_library(gst-android-app SHARED native-lib.cpp)

target_compile_features(gst-android-app PRIVATE cxx_std_20)
target_link_libraries(gst-android-app PRIVATE
    GStreamer::mobile
    ${LOG_LIBRARY}
)

set_target_properties(gst-android-app PROPERTIES
    C_VISIBILITY_PRESET hidden
    CXX_VISIBILITY_PRESET hidden
)
