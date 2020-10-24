import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel.CURRENT
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform") version "1.4.10"
  id("com.github.ben-manes.versions") version "0.33.0"
}

group = "app.nameplaceholder"
version = "0.1"

repositories {
  mavenCentral()
}

kotlin {
  linuxX64()
  mingwX64()
  macosX64()

  targets.withType(KotlinNativeTarget::class).all {
    binaries { executable(listOf(RELEASE)) }
  }

  targets.all {
    compilations.all {
      kotlinOptions {
        allWarningsAsErrors = false
        freeCompilerArgs = listOf("-Xallow-result-return-type")
      }
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
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
      useExperimentalAnnotation("kotlin.RequiresOptIn")
      useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
  fun isNonStable(version: String): Boolean {
    val stableKeyword =
      listOf("FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
  }
  resolutionStrategy {
    componentSelection {
      all {
        if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
  checkForGradleUpdate = true
  gradleReleaseChannel = CURRENT.id
}

tasks.wrapper {
  gradleVersion = "6.7"
  distributionType = Wrapper.DistributionType.ALL
}
