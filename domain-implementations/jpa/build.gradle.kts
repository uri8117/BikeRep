plugins {
    id("java")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(project(":utilities"))
    implementation(project(":utilities"))
    implementation("org.hibernate:hibernate-core:6.5.0.Final")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

tasks.test {
    useJUnitPlatform()
}