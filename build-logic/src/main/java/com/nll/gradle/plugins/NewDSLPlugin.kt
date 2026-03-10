package com.nll.gradle.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinBaseApiPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File


abstract class NewDSLPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val tomlLibs = project.extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        applyCommonDependenciesConfig(tomlLibs, project)

        project.plugins.configureEach {
            when (this) {
                is AppPlugin -> {
                    configureWithAppPlugin(tomlLibs, project)
                }

                is LibraryPlugin -> {
                    configureWithLibraryPlugin(tomlLibs, project)
                }

                is KotlinBasePluginWrapper, is KotlinBaseApiPlugin -> {
                    configureWithKotlinPlugin(tomlLibs, project)
                }

                is JavaGradlePluginPlugin -> {
                    //Unused
                }

                is JavaPlugin -> {
                    //Unused
                }

            }
        }


    }


    private fun configureWithAppPlugin(tomlLibs: VersionCatalog, project: Project) {
        project.extensions.getByType<ApplicationExtension>().apply {

            configureAndroidBaseOptions(tomlLibs, project)

             defaultConfig {
                targetSdk = tomlLibs.findVersion("targetSdkVersion").get().toString().toInt()
            }


            buildTypes {
                release { }
                debug { }
                configureEach {  }
            }
        }

        project.extensions.getByType<ApplicationAndroidComponentsExtension>().apply {
            onVariants { variant ->

            }
        }
    }

    private fun configureWithLibraryPlugin(tomlLibs: VersionCatalog, project: Project) {
        project.extensions.getByType<LibraryExtension>().apply {
            configureAndroidBaseOptions(tomlLibs, project)
            defaultConfig {
            }
        }

        project.extensions.getByType<LibraryAndroidComponentsExtension>().apply {
            finalizeDsl {

            }
        }
    }

    private fun Any.configureAndroidBaseOptions(tomlLibs: VersionCatalog, project: Project) {
        // Workaround to avoid specifying the parametrized types of CommonExtension explicitly
        // So we can clean up the parameters in AGP
        // The compiler can infer that this is CommonExtension from these checks
        if (this !is ApplicationExtension && this !is LibraryExtension && this !is TestExtension) {
            throw IllegalArgumentException("Unexpected extension: $this")
        }

        val javaVersion = tomlLibs.findVersion("javaVersion").get().toString()
        val javaVersionEnum = JavaVersion.toVersion(javaVersion)
        val compileSdkVersion = tomlLibs.findVersion("compileSdkVersion").get().toString().toInt()
        val compileSdkMinorApiLevel = tomlLibs.findVersion("compileSdkMinorApiLevel").get().toString().toInt()
        val minSdkVersion = tomlLibs.findVersion("minSdkVersion").get().toString().toInt()

        //buildToolsVersion = project.defaultAndroidConfig.buildToolsVersion

        ndkVersion =  tomlLibs.findVersion("ndkVersion").get().toString()

        defaultConfig.apply {
            minSdk = minSdkVersion
            vectorDrawables.useSupportLibrary = true
        }

        buildFeatures.apply {

        }

        compileOptions.apply {
            sourceCompatibility = javaVersionEnum
            targetCompatibility = javaVersionEnum
        }

        compileSdk {
            version = release(compileSdkVersion) {
                if (compileSdkMinorApiLevel > 0) {
                    minorApiLevel = compileSdkMinorApiLevel
                }
            }
        }
        externalNativeBuild.cmake{
            buildStagingDirectory = File(System.getProperty("user.home"))
                .resolve(".build-cxx")
                .resolve(project.rootProject.name)
                .resolve(project.name)
        }



    }

    private fun applyCommonDependenciesConfig(tomlLibs: VersionCatalog, project: Project) = project.dependencies {


    }

    private fun configureWithKotlinPlugin(tomlLibs: VersionCatalog, project: Project) {
        val libsJavaVersion = tomlLibs.findVersion("javaVersion").get().toString()
        project.tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions.jvmTarget.set(JvmTarget.fromTarget(libsJavaVersion))
        }
    }

}