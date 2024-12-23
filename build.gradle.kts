plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
    alias(libs.plugins.lombok)
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
    implementation(libs.springboot.starter.actuator)

    developmentOnly(libs.bundles.springboot.development)

    runtimeOnly(libs.postgresql)

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
