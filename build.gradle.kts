// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint)
}

buildscript {
    repositories {
        // Do not use `mavenCentral()`, it prevents Dependabot from working properly
        maven {
            url = uri("https://repo1.maven.org/maven2")
            url = uri("https://jitpack.io")
        }
    }
    dependencies {
        classpath( libs.realm.gradle.plugin)
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.allWarningsAsErrors =
            project.findProperty("allWarningsAsErrors")?.toString()?.toBoolean() ?: true
    }

    tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask>().configureEach {
        workerMaxHeapSize.set("2G")
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("0.45.1")
        android.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        verbose.set(true)

        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }

        filter {
            exclude { element -> element.file.path.contains("$buildDir/generated/") }
        }

        disabledRules.set(
            listOf(
                "indent",
                "experimental:argument-list-wrapping",
                "max-line-length",
                "parameter-list-wrapping",
                "spacing-between-declarations-with-comments",
                "no-multi-spaces",
                "experimental:spacing-between-declarations-with-annotations",
                "experimental:annotation",
                "wrapping",
                "experimental:trailing-comma",
                "experimental:comment-wrapping",
                "experimental:kdoc-wrapping",
                "string-template"
            )
        )
    }
}
