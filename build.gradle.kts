plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("co.touchlab.cklib").apply(false)
    id("com.diffplug.spotless")
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        val ktlintVersion = "0.49.1"
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/")
            ktlint(ktlintVersion)
        }
        kotlinGradle {
            target("*.kts")
            targetExclude("**/build/")
            ktlint(ktlintVersion)
        }
    }
}
