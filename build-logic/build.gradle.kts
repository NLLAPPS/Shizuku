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
        /**
         * We do not have access to actual project name here. It returns null and rootProject returns "buildSrc"
         * So ww manually add ACRPhoneProject
         */
        layout.buildDirectory.set(File("${System.getProperty("user.home")}${File.separator}.build${File.separator}ACRPhoneProject${File.separator}${rootProject.name}${File.separator}${project.name}"))
        project.logger.lifecycle("Project build-logic buildDir -> ${layout.buildDirectory.get()}")
    }
}