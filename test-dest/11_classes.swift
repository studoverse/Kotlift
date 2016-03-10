/**
 * See https://kotlinlang.org/docs/reference/classes.html
 */

public class Empty {
    init() {}
}

public class Empty2 {
    init() {

    }
}

public class Thing {
    init(name: String, height: Int32, width: Int32, cool: Bool) {

    }
}

public class Customer {
    init(name: String) {
        print("Customer initialized with value \(name)")
    }
}

public class Thing2 {
  init(name: String, height: Int32, width: Int32, cool: Bool) {
  }
}

public class House {
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
