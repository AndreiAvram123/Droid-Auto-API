import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("kapt") version "1.5.31"

}

group = "com.andrei"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven {
        setUrl( "https://mymavenrepo.com/repo/v2xm7gYVinuWT46Px3Xy/")
    }
}

dependencies {

    val mockkVersion = "1.12.2"


    //email service
    implementation ("com.sendgrid:sendgrid-java:4.8.3")


    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.3")


    implementation ("com.google.code.gson:gson:2.9.0")


    //jwt
    implementation("com.auth0:java-jwt:3.18.3")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.3")

    //database connectivity and management
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.3")
    implementation("org.postgresql:postgresql:42.3.2")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")


    implementation("org.springframework.boot:spring-boot-starter-web:2.6.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.3")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
