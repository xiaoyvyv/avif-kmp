# brew install dpkg rpm
#git clone https://chromium.googlesource.com/libyuv/libyuv

build="${prefix_dir}/libyuv"

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	rm -rf "$build"
	exit 0
else
	exit 255
fi

yuv_dir="$deps_dir/libyuv"
if [ ! -d "$yuv_dir" ]; then
  echo "not download libyuv: $yuv_dir"
  exit 255
fi

mkdir -p "${build}"
cd "${build}"

cmake -G Ninja -DCMAKE_BUILD_TYPE=Release "$yuv_dir"
ninja