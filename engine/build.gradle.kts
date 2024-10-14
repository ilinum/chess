plugins {
    java
}

tasks.register<Exec>("buildGoImage") {
    workingDir = file("$projectDir")
    commandLine("docker", "build", "-t", "engine", ".")
}

tasks.register<Exec>("runEngine") {
    dependsOn("buildGoImage")
    commandLine("docker", "run", "-p", "8080:8080", "engine")
}

tasks.named("build") {
    dependsOn("buildGoImage")
}
