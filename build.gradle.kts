plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "com.github.szgabsz91"
version = "1.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

extra["springShellVersion"] = "3.3.3"
extra["jacksonVersion"] = "2.18.2"

dependencies {
	implementation("org.springframework.shell:spring-shell-starter")
	implementation("com.fasterxml.jackson.core:jackson-core:${property("jacksonVersion")}")
	implementation("com.fasterxml.jackson.core:jackson-databind:${property("jacksonVersion")}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.shell:spring-shell-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
