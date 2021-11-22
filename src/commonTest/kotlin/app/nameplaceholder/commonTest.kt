package app.nameplaceholder

import kotlin.test.Test
import kotlin.test.assertEquals

class HelloWorldTest {
  @Test
  fun helloTest(){
    val hello = HelloWorld()

    assertEquals("HelloWorld", hello.message)
  }
}
