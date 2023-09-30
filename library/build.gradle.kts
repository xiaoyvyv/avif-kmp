import co.touchlab.cklib.gradle.CompileToBitcode.Language
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("co.touchlab.cklib")
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.custom {
        common {
            group("jni") {
                withAndroidTarget()
                withJvm()
            }
            group("skia") {
                group("native") {
                    group("ios") {
                        withIosX64()
                        withIosArm64()
                        withIosSimulatorArm64()
                    }
                }
                withJvm()
            }
        }
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val skiaMain by getting {
            dependencies {
                api("org.jetbrains.skiko:skiko:0.7.80")
            }
        }
    }
    targets.withType<KotlinNativeTarget> {
        val main by compilations.getting
        val libraryPath = file("$rootDir/library/darwin/build/ios")
        main.cinterops {
            create("avif") {
                defFile("src/nativeMain/cinterop/avif.def")

                includeDirs(file("darwin/libavif/include"))

                extraOpts("-libraryPath", "$libraryPath")

                header(file("src/nativeMain/cpp/avifImageNative.h"))

                //

                // includeDirs(file("wrapper/skia"))

                // compilerOpts("-x", "c++", "-std=c++14")

                // extraOpts("-Xsource-compiler-option", "-std=c++11")

                // extraOpts(
                //     // "-Xsource-compiler-option", "-DONLY_C_LOCALE=1",
                //     // "-Xsource-compiler-option", "-std=c99",
                //     // "-Xsource-compiler-option", "-x",
                //     // "-Xsource-compiler-option", "c++",
                //     // "-Xsource-compiler-option", "-std=c++14",
                //     // "-Xcompile-source", "${projectDir}/src/nativeMain/cpp/avifImageNative.cpp",
                // )
            }
        }
        main.kotlinOptions {
            // https://youtrack.jetbrains.com/issue/KT-39396
            freeCompilerArgs += listOf(
                // "-Xallocator=std",
                "-include-binary",
                "$libraryPath/libdav1d.a",
                "-include-binary",
                "$libraryPath/libavif.a",
            )
        }
    }
    jvmToolchain(11)
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.seiko.avif"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        ndk {
            abiFilters += listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        @Suppress("UNUSED_VARIABLE", "UnstableApiUsage")
        val release by getting {
            externalNativeBuild {
                cmake {
                    cFlags("-fvisibility=hidden")
                    cppFlags("-fvisibility=hidden")
                }
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/androidMain/CMakeLists.txt")
        }
    }
    ndkVersion = "25.1.8937393"
}

val buildLibAvifNativeKLib by tasks.creating(Exec::class) {
    group = "avif"

    inputs.files(projectDir.resolve("build-jvm-klib.sh"))

    workingDir = projectDir

    commandLine("bash", "-l", "build-jvm-klib.sh")
}

cklib {
    config.kotlinVersion = extra["kotlin.version"] as String
    create("avif") {
        language = Language.C
        srcDirs = project.files(
            file("src/nativeMain/cpp"),
        )
        headersDirs += project.files(
            file("darwin/libavif/include"),
            // file("wrapper/skia-iosSim"),
        )

        // val sdkRoot = "/Applications/Xcode.app/Contents/Developer/Platforms"
        // // val iphoneOsSdk = "$sdkRoot/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk"
        // val iphoneSimSdk = "$sdkRoot/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk"

        compilerArgs.addAll(
            listOf(
                // "-fPIC",
                // "-stdlib=libc++",
                // // "-std=gnu++20",
                // "-nostdinc++",
                // "-nostdinc",
                // "-DNANOSTL_PSTL",
                "-Wno-unused-parameter",

                // "-DSK_ALLOW_STATIC_GLOBAL_INITIALIZERS=1",
                // "-DSK_FORCE_DISTANCE_FIELD_TEXT=0",
                // "-DSK_GAMMA_APPLY_TO_A8",
                // "-DSK_GAMMA_SRGB",
                // "-DSK_SCALAR_TO_FLOAT_EXCLUDED",
                // "-DSK_SUPPORT_GPU=1",
                // "-DSK_GL",
                // "-DSK_SHAPER_HARFBUZZ_AVAILABLE",
                // "-DSK_UNICODE_AVAILABLE",
                // "-DSK_SUPPORT_OPENCL=0",
                // "-DSK_UNICODE_AVAILABLE",
                // "-DU_DISABLE_RENAMING",
                // "-DSK_USING_THIRD_PARTY_ICU",

                // "-DSK_BUILD_FOR_IOS",
                // "-DSK_SHAPER_CORETEXT_AVAILABLE",
                // "-DSK_METAL",

                // "-I${iphoneSimSdk}/usr/include/c++/v1",
                // "-I${iphoneSimSdk}/usr/include",

                // "-isysroot", iphoneSimSdk,
                // "-miphoneos-version-min=12.0"
            ),
        )
        // linkerArgs.addAll(
        //     listOf(
        //         "-sysroot", iphoneSimSdk,
        //         "-miphoneos-version-min=12.0",
        //     )
        // )

        // linkerArgs.addAll(
        //     listOf(
        //         "-sysroot",
        //         "${iphoneSimSdk}/usr/include/c++/v1",
        //         // "${iphoneSimSdk}/usr/include",
        //         // "/Applications/Xcode-15.0.0-Beta.6.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++"
        //     )
        // )
    }
}
