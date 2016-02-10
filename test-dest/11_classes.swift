/**
 * See https://kotlinlang.org/docs/reference/classes.html
 */

class Empty {
    init() {}
}

class Empty2 {
    init() {

    }
}

class Thing {
    init(name: String, height: Int, width: Int, cool: Bool) {

    }
}

class Customer {
    init(name: String) {
        print("Customer initialized with value \(name)")
    }
}

class Thing2 {
  init(name: String, height: Int, width: Int, cool: Bool) {
  }
}

class House {
    init() {
        print("Builing a new house")
    }
}

func main(args: [String]) {
    Empty()
    Empty2()
    House()
    Customer(name: "Bob")
    Customer(name: "Dan")
    Thing(name: "funky thing", height: 3, width: 5, cool: true)
    Thing2(name: "funky thing", height: 3, width: 5, cool: true)
}

main([])
