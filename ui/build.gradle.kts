plugins {
    application
    java
    id("com.gradleup.shadow") version "8.3.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("info.picocli:picocli:4.7.6")
    implementation("org.jetbrains:annotations:25.0.0")

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


application {
    mainClass.set("me.ilinskiy.chess.UIMain")
}

tasks.register<JavaExec>("runUI") {
    group = "application"
    description = "Runs the Java application with the UI."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(application.mainClass)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    archiveBaseName.set("chess-ui")
}

version = "1.4"