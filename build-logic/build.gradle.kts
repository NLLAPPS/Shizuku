plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("newdsl") {
            id = "nll.$name"
            implementationClass = "com.nll.gradle.plugins.NewDSLPlugin"
        }
        create("copyArtifacts") {
            id = "nll.$name"
            implementationClass = "com.nll.gradle.plugins.NLLCopyArtifactsPlugin"
        }
    }

    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }

    dependencies {

        compileOnly(gradleApi())

        implementation(libs.kotlin.gradlePlugin)

        implementation(libs.android.buildtoolsGradle)


    }


    allprojects {
        val rootName = gradle.parent?.rootProject?.name ?: rootProject.name
        layout.buildDirectory.set(File("${System.getProperty("user.home")}${File.separator}.build${File.separator}$rootName${File.separator}${rootProject.name}${File.separator}${project.name}"))
        project.logger.lifecycle("Project build-logic buildDir -> ${layout.buildDirectory.get()}")
    }
}