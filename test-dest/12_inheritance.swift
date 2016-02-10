class Base {
  func v() { print("ERROR") }
  func nv() { print("3") }
  init() { print("1") }
}

class Derived: Base {
  override func v() { print("4") }
  override init() { print("2") }
}

class Derived2: Base {
  override func v() { print("4") }
  override init() { print("2") }
}

func main(args: [String]) {
  let x = Derived()
  x.nv()
  x.v()
}

main([])