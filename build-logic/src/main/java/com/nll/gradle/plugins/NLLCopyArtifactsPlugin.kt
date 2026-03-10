package com.nll.gradle.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.impl.capitalizeFirstChar
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import java.util.Locale
import kotlin.jvm.java

class NLLCopyArtifactsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            project.extensions.findByType<ApplicationAndroidComponentsExtension>()?.onVariants { variant ->

                val variantName = variant.name.capitalizeFirstChar()
                val dexPath = project.layout.projectDirectory.dir("manager/src/main/assets")

                val copyTask = project.tasks.register("copyDexFor$variantName", Copy::class.java) {
                    val artifacts = variant.artifacts.get(SingleArtifact.APK)

                    from(artifacts.map { dir ->
                        project.zipTree(dir.asFileTree.matching { include("*.apk") }.singleFile)
                            .matching { include("classes.dex") }
                    })

                    into(dexPath)
                    rename { "rish_shizuku.dex" }

                    dependsOn(variant.artifacts.get(SingleArtifact.APK))
                }


                project.afterEvaluate {
                    project.tasks.named("assemble$variantName").configure {
                        finalizedBy(copyTask)
                    }
                }
            }
        }
    }
}