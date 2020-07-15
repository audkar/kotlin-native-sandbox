import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel.CURRENT

plugins {
  kotlin("multiplatform") version "1.3.72"
  id("com.github.ben-manes.versions") version "0.28.0"
}

group = "app.nameplaceholder"
version = "0.1"

repositories {
  mavenCentral()
}

kotlin {
  linuxX64 {
    binaries {
      executable(listOf(DEBUG))
    }
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
    val coroutines = "1.3.7"
    commonMain {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutines")
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
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutines")
      }
    }
  }

  sourceSets.all {
    languageSettings.apply {
      progressiveMode = true
      enableLanguageFeature("InlineClasses")
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
  gradleVersion = "6.5.1"
  distributionType = Wrapper.DistributionType.ALL
}
