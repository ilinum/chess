plugins {
    application
    java
    id("com.gradleup.shadow") version "8.3.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":engine"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains:annotations:25.0.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


application {
    mainClass.set("me.ilinskiy.chess.JSwingMain")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    archiveBaseName.set("chess")
}

version = "1.3"