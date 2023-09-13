# meson required
find_program(Meson_EXECUTABLE meson)
if(NOT Meson_EXECUTABLE)
    message(FATAL_ERROR "Meson is required")
endif()

# ninja required
find_program(Ninja_EXECUTABLE ninja)
if(NOT Ninja_EXECUTABLE)
    message(FATAL_ERROR "Ninja is required")
endif()

set(DEPENDENCY_PACKAGES_DIR ${PROJECT_NATIVE_DIR}/packages)
set(DEPENDENCY_BUILD_DIR ${PROJECT_NATIVE_DIR}/build)

# TODO:
set(MESON_CROSS ${PROJECT_NATIVE_DIR}/dav1d/package/crossfiles/aarch64-android.meson)

set(CORE_BUILD_DIR "build")
include(${DEPENDENCY_CMAKE_DIR}/FindDav1d.cmake)

