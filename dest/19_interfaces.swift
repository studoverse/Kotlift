// Simple Interface

protocol MyInterface {
  func bar() -> String
  /*fun foo(): Int {
    return 1
  }*/
}

public class Implementation: MyInterface {
  func bar() -> String {
    return "2-Implementation"
  }
  init() {
  }
}

// Interface + Inheritance

public class Parent {
  func three() -> Int {
    return 3
  }

  var four = 4
  init() {
  }
}

public class Child: Parent, MyInterface {
  let five = 5
  func six() -> String {
    return "6"
  }
  func bar() -> String {
    return "2-Child"
  }
  override init() {
  }
}

// Abstract Interface + Inheritance

public class AbstractParent {
  func three() -> Int {
    fatalError("Method is abstract")
  }

  var four = 4
  init() {
  }
}

public class AbstractChild: Parent {
  override init() {
  }
}

public class NonAbstractChild: AbstractChild, MyInterface {
  func bar() -> String {
    return "2-NonAbstractChild"
  }
  override init() {
  }
}

func main(args: [String]) {
  // Simple Interface
  let obj = Implementation()
  print(obj.foo())
  print(obj.bar())

  // Interface + Inheritance
  let child = Child()
  print(child.foo())
  print(child.bar())
  print(child.three())
  print(child.four)
  print(child.five)
  print(child.six())

  // Abstract Interface + Inheritance
  let naChild = NonAbstractChild()
  print(naChild.foo())
  print(naChild.bar())
  print(naChild.three())
  print(naChild.four)
}

main([])
