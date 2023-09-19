@file:Suppress("HasPlatformType")

import org.gradle.internal.os.OperatingSystem

val clean by tasks.creating {
    group = taskGroup
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

    outputs.dir(projectDir.resolve("build/$targetName"))

    workingDir = projectDir
    environment("TARGET", targetName)
    commandLine("bash", "-l", "scripts/build-host.sh")
}

val buildLibAvifIos by tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvif.dependsOn(this)

    onlyIf { currentOs.isMacOsX }

    inputs.files(projectDir.resolve("build-ios.sh"))
    outputs.dir(projectDir.resolve("build/ios"))

    workingDir = projectDir
    environment("IOS_TOOLCHAIN_FILE", file("ios.toolchain.cmake"))
    // https://github.com/leetal/ios-cmake/tree/master#platform-flag-options--dplatformflag
    environment("BUILD_PLATFORM", "SIMULATORARM64")
    commandLine("bash", "-l", "scripts/build-ios.sh")
}

val buildLibAvifAndroid by tasks.creating {
    group = taskGroup
    buildLibAvif.dependsOn(this)
}

fun createBuildLibAvifAndroidTask(arch: String) = tasks.creating(Exec::class) {
    group = taskGroup
    buildLibAvifAndroid.dependsOn(this)

    inputs.files(projectDir.resolve("build-android.sh"))
    outputs.dir(projectDir.resolve("build/android/$arch"))

    val toolchain = when {
        currentOs.isLinux -> "linux-x86_64"
        currentOs.isMacOsX -> "darwin-x86_64"
        currentOs.isWindows -> "windows-x86_64"
        else -> error("No Android toolchain defined for this OS: $currentOs")
    }

    workingDir = projectDir
    environment("TOOLCHAIN", toolchain)
    environment("ARCH", arch)
    commandLine("bash", "-l", "scripts/build-android.sh")
}

val buildLibAvifAndroidX64 by createBuildLibAvifAndroidTask("x86_64")
val buildLibAvifAndroidX86 by createBuildLibAvifAndroidTask("x86")
val buildLibAvifAndroidArm64V8a by createBuildLibAvifAndroidTask("arm64-v8a")
val buildLibAvifAndroidArmeabiV7a by createBuildLibAvifAndroidTask("armeabi-v7a")

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
    get() = "build"
