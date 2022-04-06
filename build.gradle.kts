
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile




plugins {
    //HEROKU GOES CRAZY WITH A BIGGER VERSION OF THIS
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    id ("org.jetbrains.kotlin.plugin.noarg") version "1.6.10"
    id ("org.jetbrains.kotlin.plugin.jpa") version "1.6.10"

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("kapt") version "1.6.10"

}

group = "com.andrei"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_15

repositories {
    mavenCentral()
    maven {
        setUrl( "https://mymavenrepo.com/repo/v2xm7gYVinuWT46Px3Xy/")
    }


}

dependencies {

    val mockkVersion = "1.12.3"

    implementation("org.geotools:gt-shapefile:26.3")

   //payment
    implementation ("com.stripe:stripe-java:20.111.0")

    //email service
    implementation ("com.sendgrid:sendgrid-java:4.9.1")

    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.5")

    implementation ("com.google.code.gson:gson:2.9.0")

    //jwt
    implementation("com.auth0:java-jwt:3.19.0")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.5")

    //database connectivity and management
    implementation ("io.lettuce:lettuce-core:6.1.8.RELEASE")
    implementation  ("org.springframework.data:spring-data-redis:2.6.3")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.5")
    implementation("org.postgresql:postgresql:42.3.3")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")


    implementation("org.springframework.boot:spring-boot-starter-web:2.6.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.5")
    testImplementation("io.mockk:mockk:$mockkVersion")


}
tasks.withType<JavaCompile>{
     inputs.files(tasks.processResources)

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
