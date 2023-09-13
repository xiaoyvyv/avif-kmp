#!/bin/bash

# This script will build dav1d for the default ABI targets supported by android.
# This script only works on linux. You must pass the path to the android NDK as
# a parameter to this script.
#
# Android NDK: https://developer.android.com/ndk/downloads
#
# The git tag below is known to work, and will occasionally be updated. Feel
# free to use a more recent commit.

set -e
if [! -x dav1d]; then
  git clone -b 1.2.1 --depth 1 https://code.videolan.org/videolan/dav1d.git
fi

cd dav1d
mkdir -p build
cd build

# This only works on darwin.
android_bin="${ANDROID_NDK_HOME}/toolchains/llvm/prebuilt/darwin-x86_64/bin"

ABI_LIST=("armeabi-v7a" "arm64-v8a" "x86" "x86_64")
ARCH_LIST=("arm" "aarch64" "x86" "x86_64")
for i in "${!ABI_LIST[@]}"; do
  abi="${ABI_LIST[i]}"
  mkdir -p "${abi}"
  cd "${abi}"
  PATH=$PATH:${android_bin} meson setup --default-library=static --buildtype=release \
    --cross-file="../../package/crossfiles/${ARCH_LIST[i]}-android.meson" \
    -Denable_tools=false -Denable_tests=false ../..
  PATH=$PATH:${android_bin} ninja
  cd ..
done

cd ../..