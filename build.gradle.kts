import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform") version "1.6.0"
}

group = "eu.kaud"
version = "0.1"

repositories {
  mavenCentral()
}

kotlin {
  linuxX64()
  linuxArm32Hfp()
  mingwX64()
  macosX64()

  targets.withType(KotlinNativeTarget::class).all {
    binaries {
      executable {
        runTask?.setStandardInput(System.`in`)
      }
    }
  }

  targets.all {
    compilations.all {
      kotlinOptions {
        allWarningsAsErrors = false
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val linuxX64Main by getting {
      dependencies {
      }
    }
  }

  sourceSets.all {
    languageSettings.apply {
      progressiveMode = true
      optIn("kotlin.RequiresOptIn")
    }
  }
}

tasks.wrapper {
  distributionType = Wrapper.DistributionType.ALL
}
