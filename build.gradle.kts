import org.jetbrains.kotlin.gradle.dsl.JvmTarget

//buildscript {
//    dependencies {
//        classpath(libs.versioning)
//    }
//}

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.loom)
    alias(libs.plugins.dokka)
//    alias(libs.plugins.validator)
//    alias(libs.plugins.ksp)
}

//if (!project.hasProperty("full")) {
//    project.gradle.startParameter.excludedTaskNames.add("kspKotlin")
//}

version = property("mod_version").toString()

repositories {
    maven("https://jitpack.io")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://repo.essential.gg/repository/maven-public")
}

dependencies {
    // To change the versions see the gradle/libs.versions.toml
    minecraft(libs.minecraft)
    implementation(libs.bundles.fabric)

    implementation(libs.bundles.included) { include(this) }
    implementation(include("gg.essential:vigilance:314")!!)
    implementation(include("gg.essential:elementa:750")!!)
    implementation(include("gg.essential:universalcraft-26.2-fabric:511")!!)

//    modApi(libs.modmenu)
//    modRuntimeOnly(libs.devauth)
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
//    implementation(project(":typing-generator"))
//    ksp(project(":typing-generator"))
}

loom {
    accessWidenerPath.set(file("src/main/resources/ctjs.accesswidener"))
}

base {
    archivesName.set(property("archives_base_name") as String)
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

//apiValidation {
//    validationDisabled = true
//    ignoredProjects += "typing-generator"
//    ignoredPackages += "com.chattriggers.ctjs.internal"
//}

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        val flkVersion = libs.versions.fabric.kotlin.get()
        val fapiVersion = libs.versions.fabric.api.get()
        val loaderVersion = libs.versions.loader.get()

        inputs.property("version", project.version)
        inputs.property("fabric_kotlin_version", flkVersion)
        inputs.property("fabric_api_version", fapiVersion)
        inputs.property("loader_version", loaderVersion)

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "fabric_kotlin_version" to flkVersion,
                "fabric_api_version" to fapiVersion,
                "loader_version" to loaderVersion
            )
        }
    }

    withType<JavaCompile>().configureEach {
        options.release.set(25)
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
            freeCompilerArgs = listOf("-Xcontext-parameters")
        }
    }

    jar {
        from("LICENSE") {
            rename { "${name}_${base.archivesName.get()}" }
        }
    }

    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

}

dokka {
    dokkaPublications.html {
        moduleName.set("ctjs")
        moduleVersion.set(project.version.toString())
        outputDirectory.set(layout.buildDirectory.dir("javadocs"))
        suppressObviousFunctions.set(true)
        suppressInheritedMembers.set(true)
    }

    dokkaSourceSets.main {
        jdkVersion.set(25)

        perPackageOption {
            matchingRegex.set("com\\.chattriggers\\.ctjs\\.internal(\$|\\.).*")
            suppress.set(true)
        }

        val branch = System.getenv("GITHUB_SHA") ?: "main"
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/asaprograms/ctjs-community/blob/$branch/src/main/kotlin")
            remoteLineSuffix.set("#L")
        }
    }
}

