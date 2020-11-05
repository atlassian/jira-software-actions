val kotlinVersion = "1.2.70"
val seleniumVersion = "3.141.59"

plugins {
    kotlin("jvm").version("1.2.70")
    `java-library`
    id("com.atlassian.performance.tools.gradle-release").version("0.7.1")
}

configurations.all {
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
        eachDependency {
            when (requested.module.toString()) {
                "commons-codec:commons-codec" -> useVersion("1.10")
                "org.jetbrains:annotations" -> useVersion("16.0.3")
            }
            when (requested.group) {
                "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
                "org.seleniumhq.selenium" -> useVersion(seleniumVersion)
                "org.slf4j" -> useVersion("1.7.25")
            }
        }
    }
}

dependencies {
    api("com.atlassian.performance.tools:jira-actions:[2.1.0,4.0.0)")
    api("com.github.stephenc.jcip:jcip-annotations:1.0-1")
    api(webdriver("selenium-api"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.glassfish:javax.json:1.1")
    implementation(webdriver("selenium-support"))
    implementation(webdriver("selenium-chrome-driver"))
    implementation("org.apache.commons:commons-math3:3.6.1")
    log4j(
        "api",
        "core",
        "slf4j-impl"
    ).forEach { implementation(it) }
    testCompile("com.atlassian.performance.tools:docker-infrastructure:0.3.3")
    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.11.1")
}

fun log4j(
    vararg modules: String
): List<String> = modules.map { module ->
    "org.apache.logging.log4j:log4j-$module:2.10.0"
}

fun webdriver(module: String): String = "org.seleniumhq.selenium:$module:$seleniumVersion"

tasks.getByName("wrapper", Wrapper::class).apply {
    gradleVersion = "5.2.1"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.getByName("test", Test::class).apply {
    exclude("**/*IT.class")
}

val testIntegration = task<Test>("testIntegration") {
    testLogging.exceptionFormat =  org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    testLogging.showStackTraces = true
    testLogging.showExceptions = true
    testLogging.showCauses = true
    maxHeapSize = "2G"
    include("**/*IT.class")
}

// Travis cannot handle this build, sadly...
tasks["check"].dependsOn(testIntegration)
