plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
}

group = "app.simplecloud.migration"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.thesimplecloud.eu/artifactory/list/gradle-release-local/")
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly(libs.kotlin.jvm)

    compileOnly(libs.simplecloud.api)
    compileOnly(libs.simplecloud.plugin)
    compileOnly(libs.simplecloud.module.permission)
    compileOnly(libs.spigot)
    compileOnly(libs.luckperms)
    implementation(libs.mongodb)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}