plugins {
    id("java")
    `java-library`
}

group = "org.saintqd"
version = "1.0-SNAPSHOT"

val annotationsVersion = "26.0.2"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.5.0-SNAPSHOT")

    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.google.guava:guava:33.3.1-jre")
    api("org.jetbrains:annotations:$annotationsVersion")
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<Jar> {

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}