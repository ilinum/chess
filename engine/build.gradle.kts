plugins {
    java
}

tasks.register<Exec>("buildGo") {
    workingDir = file("$projectDir")
    commandLine("go", "build", "-o", "${layout.buildDirectory.get().toString()}/engine", "cmd/engine/main.go")
}

tasks.register<Exec>("runEngine") {
    dependsOn("buildGo")
    commandLine(layout.buildDirectory.dir("engine").get().toString())
}

tasks.named("build") {
    dependsOn("buildGo")
}
