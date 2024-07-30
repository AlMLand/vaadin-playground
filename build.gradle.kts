import com.vaadin.gradle.vaadin

plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.vaadin") version "24.4.7"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.almland"
version = "0.0.1-SNAPSHOT"

vaadin {
    frontendDirectory = File(project.rootDir, "fronted-app")
}

extra["vaadinVersion"] = "24.4.7"
extra["poiOoxmlVersion"] = "5.3.0"
extra["flyingSaucerPdfItext5"] = "9.7.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // excel
    implementation("org.apache.poi:poi-ooxml:${property("poiOoxmlVersion")}")
    // pdf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.xhtmlrenderer:flying-saucer-pdf-itext5:${property("flyingSaucerPdfItext5")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
