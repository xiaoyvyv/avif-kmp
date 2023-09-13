import co.touchlab.cklib.gradle.CompileToBitcode.Language.C
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
        val skiaMain by getting {
            dependencies {
                api("org.jetbrains.skiko:skiko:0.7.73")
            }
        }
    }
    targets.withType<KotlinNativeTarget> {
        val main by compilations.getting
        main.cinterops {
            create("avif-kmp") {

            }
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
}

cklib {
    config.kotlinVersion = extra["kotlin.version"] as String
    create("avif-kmp") {
        language = C
        srcDirs = project.files(file("native/libavif"), file("native/common"))
    }
}
