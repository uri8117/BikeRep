/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.java-application-conventions")
    id("org.springframework.boot") version "3.1.0" // Versión del plugin de Spring Boot
    id("io.spring.dependency-management") version "1.1.0" // Gestión de dependencias de Spring
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":domain-implementations:jdbc"))

    // Spring Boot Starter para la aplicación web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Otros starters de Spring, dependiendo de tus necesidades
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Para JPA
    implementation("org.springframework.boot:spring-boot-starter-validation") // Para validación
    implementation("org.springframework.boot:spring-boot-starter-security") // Para seguridad (si lo necesitas)

    // Para las pruebas
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Incluye WebMvcTest y otras utilidades
    testImplementation("org.mockito:mockito-core:5.0.0") // Versión de Mockito
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0") // Versión de JUnit

    // Jackson para manejo de JSON (incluido en Spring Boot Starter)
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // Otras dependencias
    implementation("com.athaydes.rawhttp:rawhttp-core:2.6.0")
}


application {
    // Define the main class for the application.
    mainClass = "cat.uvic.teknos.gt3.services.App"
}
