plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("com.goebl:david-webb:1.3.0")
    implementation("org.telegram:telegrambots:4.9.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.2.4.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.2.4.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.2.4.RELEASE")
    implementation("org.json:json:20180813")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.zxing:javase:3.4.1")
    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.2.4.RELEASE")
    runtimeOnly("org.postgresql:postgresql:42.2.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.4.RELEASE")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:2.0.4.RELEASE")
    compileOnly("com.google.android:android:2.3.1")
    implementation("org.projectlombok:lombok:1.18.16")
    annotationProcessor( "org.projectlombok:lombok:1.18.16")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.16")
}



group = "ru.ixec"
version = "0.0.1-SNAPSHOT"
description = "EasyFinance"
java.sourceCompatibility = JavaVersion.VERSION_11

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
