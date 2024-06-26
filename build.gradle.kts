plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api(libs.org.jetbrains.kotlin.kotlin.stdlib.jdk8)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
}

group = "me.mapetr"
version = "1.0-SNAPSHOT"
description = "uwuEssentials"
java.sourceCompatibility = JavaVersion.VERSION_21
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
