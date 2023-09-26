cd libavif || exit 255

echo "ios_cross_file: ${IOS_CROSS_FILE}"
echo "ios_toolchain_cmake: ${IOS_TOOLCHAIN_FILE}"

# START dav1d
if ! [ -f ext/dav1d ]; then
  git clone -b 1.2.1 --depth 1 https://code.videolan.org/videolan/dav1d.git ext/dav1d
fi
cd ext/dav1d || exit 255

rm -rf "build"
mkdir "build"
cd "build" || exit 255

meson setup \
  --cross-file="${IOS_CROSS_FILE}" \
  --default-library=static \
  --buildtype=release \
  -Db_lto=false \
  -Db_ndebug=false \
  -Denable_asm=false \
  -Denable_tools=false \
  -Denable_examples=false \
  -Denable_tests=false \
  ..
ninja

cd ..
cd ../..
# END dav1d

# START avif
build_dir="_build-ios"
mkdir -p "${build_dir}"

cmake -B "${build_dir}" -G Xcode \
  -DCMAKE_TOOLCHAIN_FILE="${IOS_TOOLCHAIN_FILE}" \
  -DPLATFORM="${BUILD_PLATFORM1}" \
  -DBUILD_SHARED_LIBS=OFF \
  -DAVIF_CODEC_AOM=OFF \
  -DAVIF_LOCAL_AOM=OFF \
  -DAVIF_CODEC_AOM_DECODE=OFF \
  -DAVIF_CODEC_AOM_ENCODE=OFF \
  -DAVIF_CODEC_DAV1D=ON \
  -DAVIF_LOCAL_DAV1D=ON \
  -DAVIF_LOCAL_LIBYUV=OFF \
  -DAVIF_LOCAL_LIBSHARPYUV=OFF
cmake --build "${build_dir}" --config Release
# END avif

# START copy *.a & rm cache dir
mkdir -p "../build/ios"

cp -v "ext/dav1d/build/src/libdav1d.a" "../build/ios" || exit 255
cp -v ${build_dir}/Release-*/libavif.a "../build/ios" || exit 255

rm -rf "ext/dav1d/build"
rm -rf "${build_dir}"
# END copy *.a & rm cache dir
