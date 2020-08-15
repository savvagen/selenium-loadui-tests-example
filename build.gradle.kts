plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    id("io.qameta.allure") version "2.7.0"
}


tasks.withType(JavaCompile::class) {
    sourceCompatibility = "${JavaVersion.VERSION_1_8}"
    targetCompatibility = "${JavaVersion.VERSION_1_8}"
    options.encoding = "UTF-8"
}

repositories {
    maven(url = "https://dl.bintray.com/qameta/maven-unstable/")
    mavenCentral()
    mavenLocal()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.junit.jupiter:junit-jupiter:5.6.2")

    implementation("com.codeborne:selenide:5.13.0")
    implementation("com.browserup:browserup-proxy-core:2.0.1")
    implementation("net.lightbody.bmp:browsermob-core:2.1.5")

    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")

}


tasks.withType(Test::class){
    useJUnitPlatform()
}

tasks.withType(Wrapper::class){
    gradleVersion = "6.1.1"
}