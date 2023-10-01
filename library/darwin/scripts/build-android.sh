cd libavif || exit 255

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
  -DAVIF_LOCAL_DAV1D=ON
cmake --build "${build_dir}"
# END avif

# START copy *.a & rm cache dir
mkdir -p "${ANDROID_OUTPUT_DIR}"

cp -v ext/dav1d/build/${ABI}/src/*.a "${ANDROID_OUTPUT_DIR}" || exit 255
cp -v ${build_dir}/*.a "${ANDROID_OUTPUT_DIR}" || exit 255

rm -rf "ext/dav1d/build/${ABI}"
rm -rf "${build_dir}"
# END copy *.a & rm cache dir
