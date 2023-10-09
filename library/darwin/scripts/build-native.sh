cd libavif || exit 255

# START libyuv
if ! [ -f ext/libyuv ]; then
  git clone --single-branch https://chromium.googlesource.com/libyuv/libyuv ext/libyuv
fi
cd ext/libyuv || exit 255

rm -rf "build"
mkdir -p "build"
cd "build" || exit 255

cmake -G Ninja -DCMAKE_BUILD_TYPE=Release ..
ninja yuv

cd ..
cd ../..
# END libyuv

# START libwebp
if ! [ -f ext/libwebp ]; then
  git clone --single-branch https://chromium.googlesource.com/webm/libwebp ext/libwebp
fi
cd ext/libwebp || exit 255

rm -rf "build"
mkdir -p "build"
cd "build" || exit 255

cmake -G Ninja -DBUILD_SHARED_LIBS=OFF -DCMAKE_BUILD_TYPE=Release ..
ninja sharpyuv

cd ..
cd ../..
# END libwebp

# START dav1d
if ! [ -f ext/dav1d ]; then
  git clone -b 1.2.1 --depth 1 https://code.videolan.org/videolan/dav1d.git ext/dav1d
fi
cd ext/dav1d || exit 255

rm -rf "build"
mkdir -p "build"
cd "build" || exit 255

meson setup \
  --default-library=static \
  --buildtype release \
  -Denable_tools=false \
  -Denable_tests=false \
  ..
ninja

cd ..
cd ../..
# END dav1d

# START avif
build_dir="_build-native"
rm -rf "${build_dir}"
mkdir -p "${build_dir}"

cmake -B "${build_dir}" -G Ninja \
  -DCMAKE_BUILD_TYPE=Release \
  -DBUILD_SHARED_LIBS=OFF \
  -DAVIF_CODEC_DAV1D=ON \
  -DAVIF_LOCAL_DAV1D=ON \
  -DAVIF_LOCAL_LIBYUV=ON \
  -DAVIF_LOCAL_LIBSHARPYUV=ON
cmake --build "${build_dir}"
# END avif

# START copy *.a & rm cache dir
mkdir -p "../build/native"

cp -v "ext/libyuv/build/libyuv.a" "../build/native" || exit 255
cp -v "ext/libwebp/build/libsharpyuv.a" "../build/native" || exit 255
cp -v "ext/dav1d/build/src/libdav1d.a" "../build/native" || exit 255
cp -v "${build_dir}/libavif.a" "../build/native" || exit 255

rm -rf "ext/libyuv/build"
rm -rf "ext/libwebp/build"
rm -rf "ext/dav1d/build"
rm -rf "${build_dir}"
# END copy *.a & rm cache dir
