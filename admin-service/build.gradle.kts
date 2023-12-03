gradle.startParameter.showStacktrace = ShowStacktrace.ALWAYS

plugins {
    id("org.springframework.boot") version "3.1.6"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.ip13"
version = "1.0.0"

java {
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot starter
    implementation("org.springframework.boot:spring-boot-starter")

//    // jpa
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.postgresql:postgresql:42.6.0")
//    implementation("org.flywaydb:flyway-core:9.22.3")
//
//    // validation
//    implementation("org.springframework.boot:spring-boot-starter-validation")

    // kotlin reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // eureka
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // loadbalancer
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    // cloud config
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // retries for cloud config
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.retry:spring-retry:2.0.4")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}

extra["springCloudVersion"] = "2022.0.4"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.compileKotlin {
    kotlinOptions {
        // support for nullable types
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.ip13.main.MainApplication"
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    jvmToolchain(17)
}