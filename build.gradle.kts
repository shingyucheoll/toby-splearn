import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("net.ltgt.errorprone") version "4.3.0" // https://github.com/tbroyer/gradle-errorprone-plugin
}

group = "tobyspring"
version = "0.0.1-SNAPSHOT"
description = "splearn"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.errorprone  {
        disableAllChecks = true                                     // Error Prone 의 기능중 NullAway 만 사용하도록 다른 검사를 비활성화 합니다.
        option("NullAway:OnlyNullMarked", "true")   // @NullMarked 어노테이션이 명시적으로 붙어있는 클래스/패키지/모듈에서만 null 검사를 수행합니다.
        error("NullAway")                            // NullAway의 검사 결과를 에러 레벨로 설정합니다. 기본값 : 경고(warning)
        option("NullAway:JSpecifyMode", "true")     // https://github.com/uber/NullAway/wiki/JSpecify-Support JSpecify 표준을 사용합니다
    }
    // Keep a JDK 17 baseline
    options.release = 17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jspecify:jspecify:1.0.0") // https://jspecify.dev/
    errorprone("com.google.errorprone:error_prone_core:2.42.0") // https://github.com/google/error-prone
    errorprone("com.uber.nullaway:nullaway:0.12.12") // https://github.com/uber/NullAway
}

tasks.withType<Test> {
    useJUnitPlatform()
}