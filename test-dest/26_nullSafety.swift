// See https://kotlinlang.org/docs/reference/nil-safety.html

public class Department {
  var head: User? = nil
  init() {
  }
}

public class User {
  var name: String?

  var department: Department? = nil
  init(name: String? = nil) {
    self.name = name
  }
}

func main(args: [String]) {
  var a: String = "abc"
  //a = nil // compilation error

  var b: String? = "abc"
  b = nil // ok

  print("a.length: \(a.length) (should be 3)")

  //let blInvalid = b.length // error: variable 'b' can be nil

  let bl = b != nil ? b!.length : -1
  print("bl: \(bl) (should be -1)")

  if b != nil && b!.length > 0 {
    print("ERROR: String of length \(b!.length)")
  } else {
    print("OK: Empty string")
  }

  print("\(b?.length) should be nil")

  // Classes
  let bob = User(name: "Bob")
  let john = User(name: "John")
  let marketing = Department()
  bob.department = marketing
  marketing.head = john
  print("\(bob.department?.head?.name) should be John")

  b = "new b value"
  print("\(b!.length) should be 11")

  let aInt: Int32? = a as? Int32
  print("\(aInt) should be nil")
}

main([])
