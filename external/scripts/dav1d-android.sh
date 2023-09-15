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

android_toolchain=$(echo "${ANDROID_NDK_HOME}/toolchains/llvm/prebuilt/"*)
echo "android toolchains: $android_toolchain"

if [ -d "$android_toolchain" ]; then
  export PATH="$PATH:${android_toolchain}/bin"
else
  echo "not find android toolchains: $android_toolchain"
  exit 255
fi

mkdir -p "${build}"
cd "${build}"

abi_list=("armeabi-v7a" "arm64-v8a" "x86" "x86_64")
arch_list=("arm" "aarch64" "x86" "x86_64")
for i in "${!abi_list[@]}"; do
  abi="${abi_list[i]}"
    mkdir -p "${abi}"
    cd "${abi}"
    meson setup \
     --cross-file="$david_dir/package/crossfiles/${arch_list[i]}-android.meson" \
     --default-library=static \
     --buildtype=release \
     -Denable_tools=false \
     -Denable_tests=false \
     "$david_dir"
    ninja
    cd ..
done
