rootProject.name = "kotlin-native-sandbox"

sourceControl {
  gitRepository(uri("https://github.com/audkar/kotest.git")) {
    producesModule("io.kotest:kotest-assertions-core")
  }
}
