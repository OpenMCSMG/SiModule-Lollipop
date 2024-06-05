val funcName = "lollipop"
val group = "cn.cyanbukkit.${funcName}"
version = "0.1"
val mainPlugin = "SiModuleGame"

bukkit {
    name = rootProject.name
    description = "吃过棒棒糖么？"
    authors = listOf("CyanBukkit")
    website = "https://cyanbukkit.cn"
    main = "${group}.cyanlib.launcher.CyanPluginLauncher"
    loadBefore = listOf(mainPlugin)
}

plugins {
    java
    kotlin("jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    maven("https://nexus.cyanbukkit.cn/repository/maven-public/")
    maven("https://maven.elmakers.com/repository")
}

dependencies {
    compileOnly("org.apache.commons:commons-lang3:3.12.0")
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs") { include("*.jar") })

}

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
}
