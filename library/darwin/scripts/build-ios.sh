cd libavif || exit 255

build_dir="_build-ios"
mkdir -p ${build_dir}

cmake -B ${build_dir} -G Xcode \
  -DCMAKE_TOOLCHAIN_FILE="$IOS_TOOLCHAIN_FILE" \
  -DPLATFORM="${BUILD_PLATFORM}" \
  -DBUILD_SHARED_LIBS=OFF \
  -DAVIF_CODEC_AOM=ON \
  -DAVIF_LOCAL_AOM=ON \
  -DAVIF_CODEC_AOM_DECODE=OFF \
  -DAVIF_CODEC_AOM_ENCODE=ON \
  -DAVIF_CODEC_DAV1D=ON \
  -DAVIF_LOCAL_DAV1D=ON \
  -DAVIF_LOCAL_LIBYUV=ON \
  -DAVIF_LOCAL_LIBSHARPYUV=ON
cmake --build ${build_dir} --config Release

mkdir -p ../build/ios
cp -v ${build_dir}/Release-*/* ../build/ios/ || exit 255

rm -rf _build-ios
