pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Machina"
include(":app")
include(":terminal-emulator")
include(":terminal-view")
include(":termux-shared")

project(":terminal-emulator").projectDir = file("termux-kotlin-app/terminal-emulator")
project(":terminal-view").projectDir = file("termux-kotlin-app/terminal-view")
project(":termux-shared").projectDir = file("termux-kotlin-app/termux-shared")
