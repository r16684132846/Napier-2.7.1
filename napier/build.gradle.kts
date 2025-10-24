import dependencies.Dep
import dependencies.Versions
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

apply(from = rootProject.file("./gradle/publish.gradle.kts"))

kotlin {
    androidTarget {
        publishAllLibraryVariants()
    }
    js(IR) {
        browser()
        nodejs()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()
    jvm()

    // darwin
    if (ideaActive.not()) {
        // intel
        macosX64()
        iosX64()
        watchosX64()
        tvosX64()

        // apple silicon
        macosArm64()
        iosArm64()
        watchosArm64()
        tvosArm64()
        iosSimulatorArm64()
        watchosSimulatorArm64()
        tvosSimulatorArm64()
    } else {
        if (isAppleSilicon) {
            // apple silicon
            macosArm64()
            iosArm64()
            watchosArm64()
            tvosArm64()
            iosSimulatorArm64()
            watchosSimulatorArm64()
            tvosSimulatorArm64()
        } else {
            // intel
            macosX64()
            iosX64()
            watchosX64()
            tvosX64()
        }
    }
    // 添加OHOS编译目标
    val ohosSdkHome = System.getenv("OHOS_SDK_HOME") ?: findProperty("OHOS_SDK_HOME")?.toString()
    val ohosNdkHome = System.getenv("OHOS_NDK_HOME") ?: findProperty("OHOS_NDK_HOME")?.toString() ?: ohosSdkHome?.plus("/native")

    if (ohosSdkHome != null && ohosNdkHome != null) {
        println("OHOS SDK detected. Enabling OHOS target.")
        println("OHOS_SDK_HOME: $ohosSdkHome")
        println("OHOS_NDK_HOME: $ohosNdkHome")
        ohosArm64() {
            compilations.getByName("main") {
                cinterops {
                    create("hilog") {
                        defFile = File(projectDir, "src/ohosArm64Main/nativeInterop/cinterop/hilog.def")
                    }
                }
            }
            binaries {
                all {
                    linkerOpts("-lhilog_ndk.z",
                        "$ohosNdkHome/llvm/lib/aarch64-linux-ohos/libunwind.a")
                }
            }
        }
    } else {
        println("OHOS SDK not found. To enable OHOS support, set OHOS_SDK_HOME and OHOS_NDK_HOME environment variables.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dep.Kotlin.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dep.Test.common)
                implementation(Dep.Test.annotation)
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(Dep.Test.jvm)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(Dep.Kotlin.js)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Dep.Test.js)
            }
        }
        val wasmJsMain by getting
        val wasmJsTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(Dep.Kotlin.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dep.Test.jvm)
            }
        }

        // Create OHOS source set
        val ohosMain by creating {
            dependsOn(commonMain)
        }
        val ohosTest by creating {
            dependsOn(commonTest)
        }

        val darwinMain by creating {
            dependsOn(commonMain)
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }

        // darwin
        if (ideaActive.not()) {
            // intel
            val macosX64Main by getting {
                dependsOn(darwinMain)
            }
            val macosX64Test by getting {
                dependsOn(darwinTest)
            }
            val iosX64Main by getting {
                dependsOn(darwinMain)
            }
            val iosX64Test by getting {
                dependsOn(darwinTest)
            }
            val watchosX64Main by getting {
                dependsOn(darwinMain)
            }
            val watchosX64Test by getting {
                dependsOn(darwinTest)
            }
            val tvosX64Main by getting {
                dependsOn(darwinMain)
            }
            val tvosX64Test by getting {
                dependsOn(darwinTest)
            }

            // apple silicon
            val macosArm64Main by getting {
                dependsOn(darwinMain)
            }
            val macosArm64Test by getting {
                dependsOn(darwinTest)
            }
            val iosArm64Main by getting {
                dependsOn(darwinMain)
            }
            val iosArm64Test by getting {
                dependsOn(darwinTest)
            }
            val watchosArm64Main by getting {
                dependsOn(darwinMain)
            }
            val watchosArm64Test by getting {
                dependsOn(darwinTest)
            }
            val tvosArm64Main by getting {
                dependsOn(darwinMain)
            }
            val tvosArm64Test by getting {
                dependsOn(darwinTest)
            }
            val iosSimulatorArm64Main by getting {
                dependsOn(darwinMain)
            }
            val iosSimulatorArm64Test by getting {
                dependsOn(darwinTest)
            }
            val watchosSimulatorArm64Main by getting {
                dependsOn(darwinMain)
            }
            val watchosSimulatorArm64Test by getting {
                dependsOn(darwinTest)
            }
            val tvosSimulatorArm64Main by getting {
                dependsOn(darwinMain)
            }
            val tvosSimulatorArm64Test by getting {
                dependsOn(darwinTest)
            }

            // OHOS support
            if (System.getenv("OHOS_SDK_HOME") != null) {
                val ohosArm64Main by getting {
                    dependsOn(ohosMain)
                }
                val ohosArm64Test by getting {
                    dependsOn(ohosTest)
                }
            }
        } else {
            if (isAppleSilicon) {
                // apple silicon
                val macosArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val macosArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val iosArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val iosArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val watchosArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val watchosArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val tvosArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val tvosArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val iosSimulatorArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val iosSimulatorArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val watchosSimulatorArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val watchosSimulatorArm64Test by getting {
                    dependsOn(darwinTest)
                }
                val tvosSimulatorArm64Main by getting {
                    dependsOn(darwinMain)
                }
                val tvosSimulatorArm64Test by getting {
                    dependsOn(darwinTest)
                }

                // OHOS support
                if (System.getenv("OHOS_SDK_HOME") != null) {
                    val ohosArm64Main by getting {
                        dependsOn(ohosMain)
                    }
                    val ohosArm64Test by getting {
                        dependsOn(ohosTest)
                    }
                }
            } else {
                // intel
                val macosX64Main by getting {
                    dependsOn(darwinMain)
                }
                val macosX64Test by getting {
                    dependsOn(darwinTest)
                }
                val iosX64Main by getting {
                    dependsOn(darwinMain)
                }
                val iosX64Test by getting {
                    dependsOn(darwinTest)
                }
                val watchosX64Main by getting {
                    dependsOn(darwinMain)
                }
                val watchosX64Test by getting {
                    dependsOn(darwinTest)
                }
                val tvosX64Main by getting {
                    dependsOn(darwinMain)
                }
                val tvosX64Test by getting {
                    dependsOn(darwinTest)
                }

                // OHOS support
                if (System.getenv("OHOS_SDK_HOME") != null) {
                    val ohosArm64Main by getting {
                        dependsOn(ohosMain)
                    }
                    val ohosArm64Test by getting {
                        dependsOn(ohosTest)
                    }
                }
            }
        }
    }
}

android {
    compileSdk = Versions.compileSdkVersion
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        namespace = "io.github.aakira.napier"
        minSdk = Versions.minSdkVersion
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
