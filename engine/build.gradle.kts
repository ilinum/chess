plugins {
    java
}

tasks.register<Exec>("buildGoImage") {
    workingDir = file("$projectDir")
    commandLine("docker", "build", "-t", "engine", ".")
}

tasks.register<Exec>("stopEngine") {
    commandLine("bash", "-c", "docker stop $(docker ps -q --filter ancestor=engine)")
}

tasks.register<Exec>("startEngine") {
    dependsOn("buildGoImage")
    commandLine("docker", "run", "-d", "-p", "8080:8080", "engine")
}

tasks.named("build") {
    dependsOn("buildGoImage")
}
