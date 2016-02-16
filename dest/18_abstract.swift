class Base {
  func f() {}
  func g() {
    print("g() called")
  }
  init() {
  }
}

class Derived: Base {
  override func f() {
    fatalError("Method is abstract")
  }
  override init() {
  }
}

class Derived2: Base {
  override func f() {
    print("f() called")
  }
  override init() {
  }
}

func main(args: [String]) {
  Derived2().f()
  Derived2().g()
}

main([])
