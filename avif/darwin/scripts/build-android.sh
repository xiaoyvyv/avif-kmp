cd libavif || exit 255


# START libyuv
if ! [ -f ext/libyuv ]; then
  git clone --single-branch https://chromium.googlesource.com/libyuv/libyuv ext/libyuv
fi
cd ext/libyuv || exit 255

libyuv_build_dir="build/${ABI}"
rm -rf "${libyuv_build_dir}"
mkdir -p "${libyuv_build_dir}"

cmake -B "${libyuv_build_dir}" \
    -DCMAKE_TOOLCHAIN_FILE="${ANDROID_NDK}/build/cmake/android.toolchain.cmake" \
    -DANDROID_ABI="${ABI}" \
    -DANDROID_STL=c++_shared \
    -DANDROID_PLATFORM="android-${ANDROID_MIN_SDK}" \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_SYSTEM_NAME=Android
cmake --build "${libyuv_build_dir}"

cd ../..
# END libyuv

# START libwebp
if ! [ -f ext/libwebp ]; then
  git clone --single-branch https://chromium.googlesource.com/webm/libwebp ext/libwebp
fi
cd ext/libwebp || exit 255

libwebp_build_dir="build"
rm -rf "${libwebp_build_dir}"
mkdir -p "${libwebp_build_dir}"

cmake -B "${libwebp_build_dir}" \
    -DCMAKE_TOOLCHAIN_FILE="${ANDROID_NDK}/build/cmake/android.toolchain.cmake" \
    -DANDROID_ABI="${ABI}" \
    -DANDROID_STL=c++_shared \
    -DANDROID_PLATFORM="android-${ANDROID_MIN_SDK}" \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_SYSTEM_NAME=Android
cmake --build "${libwebp_build_dir}"

cd ../..
# END libwebp

# START dav1d
if ! [ -f ext/dav1d ]; then
  git clone -b 1.2.1 --depth 1 https://code.videolan.org/videolan/dav1d.git ext/dav1d
fi
cd ext/dav1d || exit 255

rm -rf "build/${ABI}"
mkdir -p "build/${ABI}"
cd "build/${ABI}" || exit 255

android_toolchain="${ANDROID_NDK}/toolchains/llvm/prebuilt/${TOOLCHAIN}"
android_bin="${android_toolchain}/bin"

echo "build android abi: ${ABI}"
echo "android_toolchain: ${android_toolchain}"
echo "android_cross_file: ${ANDROID_CROSS_FILE}"

PATH=$PATH:${android_bin} meson setup \
  --default-library=static \
  --buildtype=release \
  --cross-file="${ANDROID_CROSS_FILE}" \
  -Db_lto=false \
  -Db_ndebug=false \
  -Denable_asm=false \
  -Denable_tools=false \
  -Denable_examples=false \
  -Denable_tests=false \
  ../..
PATH=$PATH:${android_bin} ninja

cd ../..
cd ../..
# END dav1d

# START avif
build_dir="_build-android_${ABI}"
rm -rf "${build_dir}"
mkdir -p "${build_dir}"

cmake -B "${build_dir}" \
  -DCMAKE_TOOLCHAIN_FILE="${ANDROID_NDK}/build/cmake/android.toolchain.cmake" \
  -DANDROID_ABI="${ABI}" \
  -DANDROID_STL=c++_shared \
  -DANDROID_PLATFORM="android-${ANDROID_MIN_SDK}" \
  -DCMAKE_SYSTEM_NAME=Android \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DAVIF_CODEC_DAV1D=ON \
  -DAVIF_LOCAL_DAV1D=ON \
  -DAVIF_LOCAL_LIBYUV=ON \
  -DAVIF_LOCAL_LIBSHARPYUV=ON
cmake --build "${build_dir}"
# END avif

# START copy *.a & rm cache dir
mkdir -p "${ANDROID_OUTPUT_DIR}"

#cp -v ext/libyuv/build/${ABI}/libyuv.a "${ANDROID_OUTPUT_DIR}" || exit 255
#cp -v ext/libwebp/build/libsharpyuv.a "${ANDROID_OUTPUT_DIR}" || exit 255
cp -v ext/dav1d/build/${ABI}/src/*.a "${ANDROID_OUTPUT_DIR}" || exit 255
cp -v ${build_dir}/*.a "${ANDROID_OUTPUT_DIR}" || exit 255

#rm -rf "ext/libyuv/build/${ABI}"
#rm -rf "ext/libwebp/build"
rm -rf "ext/dav1d/build/${ABI}"
rm -rf "${build_dir}"
# END copy *.a & rm cache dir
