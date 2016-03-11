// See https://kotlinlang.org/docs/reference/null-safety.html

class Department {
  var head: User? = null
}

class User(var name: String? = null) {
  var department: Department? = null
}

fun main(args: Array<String>) {
  var a: String = "abc"
  //a = null // compilation error

  var b: String? = "abc"
  b = null // ok

  println("a.length = ${a.length} (should be 3)")

  //val blInvalid = b.length // error: variable 'b' can be null

  // SWIFT: let bl = b != nil ? b!.length : -1
  val bl = if (b != null) b.length else -1
  println("bl = $bl (should be -1)")

  // SWIFT: if b != nil && b!.length > 0 {
  if (b != null && b.length > 0) {
    // SWIFT: print("ERROR: String of length \(b!.length)")
    println("ERROR: String of length ${b.length}")
  } else {
    println("OK: Empty string")
  }

  println("${b?.length} should be null")

  // Classes
  val bob = User(name = "Bob")
  val john = User(name = "John")
  val marketing = Department()
  bob.department = marketing
  marketing.head = john
  println("${bob.department?.head?.name} should be John")

  b = "new b value"
  println("${b!!.length} should be 11")

  val aInt: Int? = a as? Int
  println("$aInt should be null")
}
