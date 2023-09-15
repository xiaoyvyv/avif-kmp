#!/bin/bash -e

build="${prefix_dir}/dav1d"

if [ "$1" == "build" ]; then
	true
elif [ "$1" == "clean" ]; then
	rm -rf "$build"
	exit 0
else
	exit 255
fi

david_dir="$deps_dir/dav1d"
if [ ! -d "$david_dir" ]; then
  echo "not download dav1d: $david_dir"
  exit 255
fi

mkdir -p "${build}"
cd "${build}"

mkdir -p "native-darwin"
cd "native-darwin"

meson setup \
     --default-library=static \
     --buildtype=release \
     "$david_dir"
ninja