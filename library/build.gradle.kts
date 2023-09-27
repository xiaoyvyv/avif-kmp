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
                // extraOpts("-libraryPath", "${layout.buildDirectory.file("cklib/avif/ios_simulator_arm64").get().asFile}")
                // linkerOpts("${layout.buildDirectory.file("cklib/avif/ios_simulator_arm64").get().asFile}")

                header(file("wrapper/common/avifImageNative.h"))
            }
        }
        main.kotlinOptions {
            // https://youtrack.jetbrains.com/issue/KT-39396
            freeCompilerArgs += listOf(
                // "-native-library", "${layout.buildDirectory.file("cklib/avif/ios_simulator_arm64/avif.bc").get().asFile}",
                "-include-binary", "$libraryPath/libdav1d.a",
                "-include-binary", "$libraryPath/libavif.a",
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
        @Suppress("UnstableApiUsage")
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
            file("wrapper/common"),
        )
        headersDirs += project.files(
            file("darwin/libavif/include"),
            file("wrapper/skia/include"),
            file("wrapper/skia"),
        )
        compilerArgs.addAll(
            listOf(
                // "-nostdinc++",
                // "-Wno-unused-parameter",
                // "-Wno-unused-function",
            )
        )
    }
}
