mkdir -p deps && cd deps

# yuv
[ ! -d libyuv ] && git clone https://chromium.googlesource.com/libyuv/libyuv

# dav1d
[ ! -d dav1d ] && git clone https://github.com/videolan/dav1d --depth=1

# avif
[ ! -d libavif ] && git clone https://github.com/AOMediaCodec/libavif.git --depth=1
