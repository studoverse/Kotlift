/* Output:
Nested init
234
Nested init
com.moshbit.kotlift.Outer$Nested@4c5e43ee (or similar)
Nested init
5
78910348910
*/

// Uncommented code will not compile due to visibility modifiers

internal class Outer {
  private let a = 1
  internal let b = 2
  internal let c = 3
  let d = 4  // public by default
  internal let n = Nested()

  internal class Nested {
    internal let e: Int = 5
    init() {
      print("Nested init")
    }
  }

  private func o() -> Int {
    return 6
  }
  internal func p() -> Int {
    return 7
  }
  internal func q() -> Int {
    return 8
  }
  func r() -> Int {
    return 9
  }
  public func s() -> Int {
    return 10
  }
}

internal class Subclass: Outer {
  // a is not visible
  // b, c and d are visible
  // Nested and e are visible

  func printAll() {
    // print(a)
    print(b)
    print(c)
    print(d)

    print(Outer.Nested())
    print(Nested().e)

    // print(o())
    print(p())
    print(q())
    print(r())
    print(s())
  }
  override init() {
  }
}

public class Unrelated {
  let o: Outer

  // o.a, o.b are not visible
  // o.c and o.d are visible (same module)
  // Outer.Nested and Nested::e are not visible. In Swift they are visible, as there is no Protected.

  func printAll() {
    // print(o.a)
    // print(o.b) // This statement runs in Swift, as there is no Protected.
    print(o.c)
    print(o.d)

    /* // It is OK that the following 3 lines run in Swift:
    val nested = Outer.Nested()
    println(nested)
    println(nested.e)*/

    // print(o.o())
    // print(o.p()) // This statement runs in Swift, as there is no Protected.
    print(o.q())
    print(o.r())
    print(o.s())
  }
  init(o: Outer) {
    self.o = o
  }
}

func main(args: [String]) {
  let x = Subclass()
  x.printAll()

  let y = Unrelated(o: x)
  y.printAll()
}

main([])
