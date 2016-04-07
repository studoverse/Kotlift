import java.util.*

@Throws(java.lang.IllegalStateException::class)
fun foo() {
  throw java.lang.IllegalStateException("Do not call foo")
}

@Throws(java.lang.IllegalStateException::class) fun bar() {
  throw java.lang.IllegalStateException("Do not call bar")
}

@Throws(java.lang.IllegalStateException::class)
fun fooReturns(): String {
  throw java.lang.IllegalStateException("Do not call fooReturns")
}

@Throws(java.lang.IllegalStateException::class) fun barReturns(): Int {
  throw java.lang.IllegalStateException("Do not call bar")
}

fun main(args: Array<String>) {
  try {
    foo()
    bar()
    var x = fooReturns()
    val y = barReturns()
  } catch (e: Exception) {
    println("Success")
  }
}
