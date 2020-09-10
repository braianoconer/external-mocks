import org.gradle.api.tasks.testing.logging.TestLogEvent


plugins {
    java
    `maven-publish`
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.gorylenko.gradle-git-properties") version "2.2.2"
    id("net.researchgate.release") version "2.8.1"
}


extra["dockerImage"] = "hub.co.local/co/${rootProject.name}"


configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}


extra["springCloudVersion"] = "Hoxton.SR6"
extra["springfoxSwaggerVersion"] = "3.0.0"


dependencies {
    implementation("org.springframework.boot","spring-boot-starter-actuator")
    implementation("org.springframework.boot","spring-boot-starter-web")
    implementation("org.springframework.cloud","spring-cloud-starter")
    implementation("org.springframework.cloud","spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud","spring-cloud-starter-zipkin")
    implementation("org.springframework.cloud","spring-cloud-starter-kubernetes-config")

    compileOnly("org.projectlombok","lombok")

    implementation("io.springfox","springfox-boot-starter", "${project.extra["springfoxSwaggerVersion"]}")

    annotationProcessor("org.springframework.boot","spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok","lombok")

    testImplementation("org.springframework.boot","spring-boot-starter-test") {
        exclude (group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${project.extra["springCloudVersion"]}")
    }
}


tasks {

    test {
        useJUnitPlatform()
        testLogging.events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }

    bootJar {
        archiveFileName.set("app.jar")
        layered()
    }

    processResources {
        filesMatching("bootstrap.*") {
            expand(project.properties)
        }
    }

    register("dockerBuild") {

        group = "docker"
        dependsOn(mutableListOf("build"))

        doLast {
            dockerBuild()
        }
    }

    register("dockerPush") {

        group = "docker"
        dependsOn(mutableListOf("publish", "dockerBuild"))

        doLast {
            dockerPush()
        }
    }
}

fun dockerBuild() {

    exec {
        executable("docker")
        args("build", "-t", "${project.extra["dockerImage"]}:${project.version}", ".")
    }
}

fun dockerPush() {

    exec {
        executable("docker")
        args("push", "${project.extra["dockerImage"]}:${project.version}")
    }
}


repositories {
    mavenCentral()
    mavenLocal()
}


publishing {
    publications {
        create<MavenPublication>("main") {
            artifact(tasks.getByName("bootJar"))
        }
    }
}


release {
    preTagCommitMessage = "release "
    tagCommitMessage = ""
    newVersionCommitMessage = ""
}