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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot starter
    implementation("org.springframework.boot:spring-boot-starter")

    // spring data jpa and postgres
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.postgresql:postgresql:42.6.0")
//    implementation("org.flywaydb:flyway-core:9.22.3")
//    implementation("org.springframework.boot:spring-boot-starter-validation")

    // reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // web mvc
    implementation("org.springframework.boot:spring-boot-starter-web")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // loadbalancer
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    // eureka
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // config server
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // resilience4f
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    // for retries to config-server
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.retry:spring-retry:2.0.4")

    // security and jwt
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // for deserializing in web client
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")

    // open api
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // web-socket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // kafka
    implementation("org.springframework.kafka:spring-kafka:3.1.1")

    // tests
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.19.1")
    testImplementation("org.testcontainers:junit-jupiter:1.19.1")
    testImplementation("org.testcontainers:postgresql:1.19.1")
    testImplementation("org.springframework.security:spring-security-test:6.1.4")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.mockito:mockito-core:5.8.0")
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
}