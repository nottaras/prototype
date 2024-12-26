plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.lombok)
    alias(libs.plugins.flyway)
}

group = "com.nottaras"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.jpa)
    implementation(libs.springboot.starter.security)
    implementation(libs.springboot.starter.oauth2.resource.server)
    implementation(libs.springboot.starter.mongodb)
    implementation(libs.springboot.starter.validation)
    implementation(libs.openapi)
    implementation(libs.mapstruct)
    implementation(libs.bundles.flyway)
    implementation(libs.micrometer)

    annotationProcessor(libs.mapstruct.processor)

    developmentOnly(libs.bundles.springboot.development)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.security.test)
    testImplementation(libs.springboot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        enabled = false
    }
}
