cd libavif || exit 255

# START dav1d
if ! [ -f ext/dav1d ]; then
  git clone -b 1.2.1 --depth 1 https://code.videolan.org/videolan/dav1d.git ext/dav1d
fi

cd ext/dav1d || exit 255

mkdir -p "build/${ABI}"
cd "build/${ABI}" || exit 255

ARCH_LIST=(
  ["armeabi-v7a"]="arm"
  ["arm64-v8a"]="aarch64"
  ["x86"]="x86"
  ["x86_64"]="x86_64"
)

android_toolchain=$(echo "${ANDROID_NDK}/toolchains/llvm/prebuilt/"*)
echo "android toolchains: $android_toolchain"

android_bin="${android_toolchain}/bin"

PATH=$PATH:${android_bin} meson setup \
  --default-library=static \
  --buildtype release \
  --cross-file="../../package/crossfiles/${ARCH_LIST[ABI]}-android.meson" \
  -Denable_tools=false \
  -Denable_tests=false \
  ../..
PATH=$PATH:${android_bin} ninja

cd ../..
cd ../..
# END dav1d

# START avif
build_dir="_build-android_${ABI}"
mkdir -p "${build_dir}"

cmake -B "${build_dir}" \
  -DCMAKE_TOOLCHAIN_FILE="${ANDROID_NDK}/build/cmake/android.toolchain.cmake"  \
  -DANDROID_ABI="${ABI}" \
  -DANDROID_PLATFORM="android-${ANDROID_MIN_SDK}" \
  -DCMAKE_ANDROID_STL_TYPE=c++_shared \
  -DCMAKE_SYSTEM_NAME=Android \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DAVIF_CODEC_DAV1D=ON \
  -DAVIF_LOCAL_DAV1D=ON
cmake --build "${build_dir}"
# END avif

# START copy *.a & rm cache dir
mkdir -p "../build/android/${ABI}"

cp -v "ext/dav1d/build/${ABI}/src/libdav1d.a" "../build/android/${ABI}" || exit 255
cp -v "${build_dir}/libavif.a" "../build/android/${ABI}" || exit 255

rm -rf "ext/dav1d/build/${ABI}"
rm -rf "${build_dir}"
# END copy *.a & rm cache dir
