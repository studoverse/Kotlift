/*
 * Expected output:
 * Hello Bob
 * You are an admin, John
 * Name: Bridge (Bridge)
 */

class User(val name: String, val admin: Boolean = false) {    
  fun printHello() {
    if (!admin) {
      println("Hello " + name) 
    } else { 
      println("You are an admin, $name")
    }
  }
}

class House(val name: String, addressPrefix: String) {
  var address = ""
  
  init {
    this.address = "$addressPrefix: $name"
  }
}

fun main(args: Array<String>) {
  val bob = User(name = "Bob")
  bob.printHello()
  
  val john = User(name = "John", admin = true)
  john.printHello()

  val house = House(name = "Bridge", addressPrefix = "Name")
  println(house.address + " (" + house.name + ")")
}