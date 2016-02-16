public class User: CustomStringConvertible {
  let name: String
  var address: String
  var age: Int
  let admin: Bool

  var description: String {
    return "User(name=\(name), address=\(address), age=\(age), admin=\(admin))"
  }

  init(name: String, address: String, age: Int, admin: Bool) {
    self.name = name
    self.address = address
    self.age = age
    self.admin = admin
  }
}

func main(args: [String]) {
  let bob = User(name: "bob", address: "london", age: 45, admin: false)
  print(bob)
  bob.age += 1
  print(bob)
}

main([])
