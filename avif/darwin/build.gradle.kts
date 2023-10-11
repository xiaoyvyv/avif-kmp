@file:Suppress("HasPlatformType")

import com.android.build.gradle.LibraryExtension
import org.gradle.internal.os.OperatingSystem

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
val buildLibAvifAndroidX64 by createBuildLibAvifAndroidTask("x86_64")
val buildLibAvifAndroidX86 by createBuildLibAvifAndroidTask("x86")
val buildLibAvifAndroidArm64V8a by createBuildLibAvifAndroidTask("arm64-v8a")
val buildLibAvifAndroidArmeabiV7a by createBuildLibAvifAndroidTask("armeabi-v7a")

fun createBuildLibAvifAndroidTask(abi: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifAndroid.dependsOn(this)

    val arch = when (abi) {
        "arm64-v8a" -> "aarch64"
        "armeabi-v7a" -> "arm"
        "x86" -> "x86"
        "x86_64" -> "x86_64"
        else -> error("Unknown Android arch with abi: $abi")
    }

    val outputDir = projectDir.resolve("build/android/$abi")
    val androidCrossFile = projectDir.resolve("crossfiles/android/$arch-android.meson")

    inputs.files(projectDir.resolve("scripts/build-android.sh"))
    inputs.files(androidCrossFile)
    outputs.dir(outputDir)

    val toolchain = when {
        currentOs.isLinux -> "linux-x86_64"
        currentOs.isMacOsX -> "darwin-x86_64"
        currentOs.isWindows -> "windows-x86_64"
        else -> error("No Android toolchain defined for this OS: $currentOs")
    }

    workingDir = projectDir

    environment("ANDROID_CROSS_FILE", androidCrossFile)
    environment("TOOLCHAIN", toolchain)
    environment("ABI", abi)
    environment("ANDROID_NDK", androidExtension.ndkDirectory)
    environment("ANDROID_MIN_SDK", androidExtension.defaultConfig.minSdk ?: 21)
    environment("ANDROID_OUTPUT_DIR", outputDir)

    commandLine("bash", "-l", "scripts/build-android.sh")
}

val buildLibAvifIos by tasks.creating {
    group = taskGroup
    buildLibAvif.dependsOn(this)
}
val buildLibAvifIosX64 by createBuildLibAvifIosTask("X64")
val buildLibAvifIosArm64 by createBuildLibAvifIosTask("Arm64")
val buildLibAvifIosSimulatorArm64 by createBuildLibAvifIosTask("SimulatorArm64")

fun createBuildLibAvifIosTask(target: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifIos.dependsOn(this)

    onlyIf { currentOs.isMacOsX }

    val arch = when (target) {
        "SimulatorArm64" -> "aarch64-sim"
        "Arm64" -> "aarch64"
        "X64" -> "x86_64"
        else -> error("Unknown IOS arch with target: $target")
    }

    val iosCrossFileName = when (target) {
        "SimulatorArm64" -> "aarch64-ios-sim.meson"
        "Arm64" -> "aarch64-ios.meson"
        "X64" -> "x86_64-ios.meson"
        else -> error("Not support IOS with target: $target")
    }

    // https://github.com/leetal/ios-cmake/tree/master#platform-flag-options--dplatformflag
    val iosToolchainPlatform = when (target) {
        "SimulatorArm64" -> "SIMULATORARM64"
        "Arm64" -> "OS64"
        "X64" -> "SIMULATOR64"
        else -> error("Not support IOS with target: $target")
    }

    val outputDir = projectDir.resolve("build/ios/$arch")
    val iosCrossFile = projectDir.resolve("crossfiles/ios/$iosCrossFileName")
    val iosCmakeFile = projectDir.resolve("ios.toolchain.cmake")

    inputs.files(projectDir.resolve("scripts/build-ios.sh"))
    inputs.files(iosCrossFile)
    inputs.files(iosCmakeFile)
    outputs.dir(outputDir)

    workingDir = projectDir

    environment("IOS_CROSS_FILE", iosCrossFile)
    environment("IOS_TOOLCHAIN_FILE", iosCmakeFile)
    environment("BUILD_PLATFORM1", iosToolchainPlatform)
    environment("IOS_OUTPUT_DIR", outputDir)
    environment("ARCH", arch)

    commandLine("bash", "-l", "scripts/build-ios.sh")
}

private val androidExtension: LibraryExtension
    get() = project(":avif").extensions["android"] as LibraryExtension

private val currentOs: OperatingSystem
    get() = OperatingSystem.current()

private val taskGroup: String
    get() = "avif"
