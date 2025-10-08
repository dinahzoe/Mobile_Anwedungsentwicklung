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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Uebung1.1"
include(":app")
include(":app:aufgabe1_textformatierung")
include(":app:aufgabe2_farbbalken")
include(":app:aufgabe3_temperaturumrechnung")
include(":app:aufgabe4_taschenrechner")
include(":app:aufgabe1")
