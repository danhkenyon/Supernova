plugins {
    kotlin("jvm") version "2.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.24"
    id("com.gradleup.shadow") version "9.6.1"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    implementation("org.yaml:snakeyaml:2.5")
}

kotlin {
    jvmToolchain(25)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        archiveFileName.set("Supernova.jar")
        destinationDirectory.set(
            file("Nexus\\Hub1\\plugins")
        )

        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
