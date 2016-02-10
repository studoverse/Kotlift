/**
 * See https://kotlinlang.org/docs/reference/classes.html
 */

class Empty {
    init {}
}

class Empty2 {
    init {

    }
}

class Thing(name: String, height: Int, width: Int, cool: Boolean) {
    init {

    }
}

class Customer(name: String) {
    init {
        println("Customer initialized with value ${name}")
    }
}

class Thing2(name: String, height: Int, width: Int, cool: Boolean) {
}

class House() {
    init {
        println("Builing a new house")
    }
}

fun main(args: Array<String>) {
    Empty()
    Empty2()
    House()
    Customer(name = "Bob")
    Customer(name = "Dan")
    Thing(name = "funky thing", height = 3, width = 5, cool = true)
    Thing2(name = "funky thing", height = 3, width = 5, cool = true)
}