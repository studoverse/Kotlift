/*
 * Expected output:
 * Hello Bob
 * You are an admin, John
 * Name: Bridge (Bridge)
 */

public class User {
  let name: String
  let admin: Bool

  func printHello() {
    if !admin {
      print("Hello " + name)
    } else {
      print("You are an admin, \(name)")
    }
  }
  init(name: String, admin: Bool = false) {
    self.name = name
    self.admin = admin
  }
}

public class House {
  let name: String

  var address = ""

  init(name: String, addressPrefix: String) {
    self.name = name
    self.address = "\(addressPrefix): \(name)"
  }
}

func main(args: [String]) {
  let bob = User(name: "Bob")
  bob.printHello()

  let john = User(name: "John", admin: true)
  john.printHello()

  let house = House(name: "Bridge", addressPrefix: "Name")
  print(house.address + " (" + house.name + ")")
}

main([])
