plugins {
    kotlin("jvm") version "2.1.0"
    application
    id("com.gradleup.shadow") version "8.3.6"
}

group = "net.hatomg"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("stdlib"))
}

application {
    mainClass.set("net.hatomg.MainKt")
}

tasks {
    shadowJar {
        archiveBaseName.set("backup_management")
        archiveVersion.set(version.toString())
        manifest {
            attributes(
                "Main-Class" to "net.hatomg.MainKt"
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
