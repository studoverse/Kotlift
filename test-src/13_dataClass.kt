data class User(val name: String, var address: String, var age: Int, val admin: Boolean)

fun main(args: Array<String>) {
  val bob = User(name = "bob", address = "london", age = 45, admin = false)
  println(bob.toString())
  bob.age += 1
  println(bob.toString())
}