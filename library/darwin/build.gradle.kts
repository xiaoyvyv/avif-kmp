@file:Suppress("HasPlatformType", "PropertyName")

import com.android.build.gradle.LibraryExtension
import org.gradle.internal.os.OperatingSystem

val clean by tasks.creating {
    group = "build"
    doLast {
        delete(projectDir.resolve("build"))
    }
}

val buildLibAvif by tasks.creating {
    group = taskGroup
}

val buildLibAvifNative by tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvif.dependsOn(this)

    inputs.files(projectDir.resolve("scripts/build-native.sh"))
    outputs.dir(projectDir.resolve("build/native"))

    workingDir = projectDir

    commandLine("bash", "-l", "scripts/build-native.sh")
}

val buildLibAvifAndroid by tasks.creating {
    group = taskGroup
    buildLibAvif.dependsOn(this)
}
val buildLibAvifAndroid_x86_64 by createBuildLibAvifAndroidTask("x86_64")
val buildLibAvifAndroid_x86 by createBuildLibAvifAndroidTask("x86")
val buildLibAvifAndroid_arm64_v8a by createBuildLibAvifAndroidTask("arm64-v8a")
val buildLibAvifAndroid_armeabi_v7a by createBuildLibAvifAndroidTask("armeabi-v7a")

val buildLibAvifIos by tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvif.dependsOn(this)

    onlyIf { currentOs.isMacOsX }

    inputs.files(projectDir.resolve("scripts/build-ios.sh"))
    inputs.files(projectDir.resolve("crossfiles"))
    inputs.files(projectDir.resolve("ios.toolchain.cmake"))
    outputs.dir(projectDir.resolve("build/ios"))

    workingDir = projectDir
    environment("IOS_CROSS_FILE", file("crossfiles/iphone_simulator.meson"))

    // https://github.com/leetal/ios-cmake/tree/master#platform-flag-options--dplatformflag
    environment("IOS_TOOLCHAIN_FILE", file("ios.toolchain.cmake"))
    environment("BUILD_PLATFORM1", "SIMULATORARM64")

    commandLine("bash", "-l", "scripts/build-ios.sh")
}

fun createBuildLibAvifAndroidTask(abi: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifAndroid.dependsOn(this)

    inputs.files(projectDir.resolve("scripts/build-android.sh"))
    outputs.dir(projectDir.resolve("build/android/$abi"))

    val toolchain = when {
        currentOs.isLinux -> "linux-x86_64"
        currentOs.isMacOsX -> "darwin-x86_64"
        currentOs.isWindows -> "windows-x86_64"
        else -> error("No Android toolchain defined for this OS: $currentOs")
    }

    workingDir = projectDir

    environment("TOOLCHAIN", toolchain)
    environment("ABI", abi)
    environment("ANDROID_NDK", androidExtension.ndkDirectory)
    environment("ANDROID_MIN_SDK", androidExtension.defaultConfig.minSdk ?: 21)

    commandLine("bash", "-l", "scripts/build-android.sh")
}

private val androidExtension: LibraryExtension
    get() = project(":library").extensions["android"] as LibraryExtension

private val currentOs: OperatingSystem
    get() = OperatingSystem.current()

private val targetName: String
    get() = when {
        currentOs.isLinux -> "linux"
        currentOs.isMacOsX -> "darwin"
        currentOs.isWindows -> "mingw"
        else -> error("Unsupported OS $currentOs")
    }

private val taskGroup: String
    get() = "avif"
