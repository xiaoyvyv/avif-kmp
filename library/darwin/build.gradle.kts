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

val buildLibAvifHost by tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvif.dependsOn(this)

    inputs.files(projectDir.resolve("scripts/build-host.sh"))
    outputs.dir(projectDir.resolve("build/$targetName"))

    workingDir = projectDir
    environment("TARGET", targetName)
    commandLine("bash", "-l", "scripts/build-host.sh")
}

val buildLibAvifIos by tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvif.dependsOn(this)
}

val buildLibAvifAndroid by tasks.creating {
    group = taskGroup
    buildLibAvif.dependsOn(this)
}
val buildLibAvifAndroid_x86_64 by createBuildLibAvifAndroidTask("x86_64")
val buildLibAvifAndroid_x86 by createBuildLibAvifAndroidTask("x86")
val buildLibAvifAndroid_arm64_v8a by createBuildLibAvifAndroidTask("arm64-v8a")
val buildLibAvifAndroid_armeabi_v7a by createBuildLibAvifAndroidTask("armeabi-v7a")

fun createBuildLibAvifIosTask(abi: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifIos.dependsOn(this)

    onlyIf { currentOs.isMacOsX }

    inputs.files(projectDir.resolve("scripts/build-ios.sh"))
    outputs.dir(projectDir.resolve("build/ios"))

    workingDir = projectDir
    environment("IOS_TOOLCHAIN_FILE", file("ios.toolchain.cmake"))
    // https://github.com/leetal/ios-cmake/tree/master#platform-flag-options--dplatformflag
    environment("BUILD_PLATFORM", "SIMULATORARM64")
    commandLine("bash", "-l", "scripts/build-ios.sh")
}

fun createBuildLibAvifAndroidTask(abi: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifAndroid.dependsOn(this)

    inputs.files(projectDir.resolve( "scripts/build-android.sh"))
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
    environment("ANDROID_MIN_SDK", androidExtension.defaultConfig.minSdk!!)
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
