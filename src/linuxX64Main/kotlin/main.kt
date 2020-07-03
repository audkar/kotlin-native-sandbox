import app.nameplaceholder.HelloWorld
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  println(HelloWorld().message)
}
