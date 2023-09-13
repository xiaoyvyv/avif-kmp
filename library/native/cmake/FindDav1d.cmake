include(ExternalProject)

set(DAV1D_LIBRARY
        ${DEPENDENCY_BUILD_DIR}/dav1d/${ANDROID_ABI}/src/libdav1d.a
)
set(DAV1D_INCLUDE_DIR
        ${DEPENDENCY_PACKAGES_DIR}/dav1d/include
        ${DEPENDENCY_BUILD_DIR}/dav1d/${ANDROID_ABI}/include)
set(DAV1D_VERSION "1.2.1")

ExternalProject_Add(dav1d
        GIT_REPOSITORY https://code.videolan.org/videolan/dav1d.git
        SOURCE_DIR ${DEPENDENCY_PACKAGES_DIR}/dav1d
        BINARY_DIR ${DEPENDENCY_BUILD_DIR}/dav1d
        CONFIGURE_COMMAND meson
        --buildtype=release
        --default-library=static
        --cross-file=${MESON_CROSS}
        --libdir=lib
        -Denable_tools=false
        -Denable_examples=false
        -Denable_tests=false
        BUILD_COMMAND ninja
        INSTALL_COMMAND ninja install)

set_target_properties(dav1d PROPERTIES FOLDER "External Projects")

#include(FindPackageHandleStandardArgs)
#find_package_handle_standard_args(Dav1d
#        REQUIRED_VARS DAV1D_LIBRARY DAV1D_INCLUDE_DIR
#        VERSION_VAR DAV1D_VERSION)
#
#if (DAV1D_FOUND)
#    set(DAV1D_INCLUDE_DIRS ${DAV1D_INCLUDE_DIR})
#    set(DAV1D_LIBRARIES ${DAV1D_LIBRARY})
#endif ()
#
#mark_as_advanced(DAV1D_INCLUDE_DIR DAV1D_LIBRARY)