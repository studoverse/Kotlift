fun x(): String {
	return "return value"
}

fun main(args: Array<String>) {
    val name = "Bob"
    var name2 = "Dan"
    println("Hello, $name!")
    println("true = ${true}")
    println("false = ${1 == 2}")
    println("function call: ${x()}")
    println("my name is $name :)")
    println("multiple: $name, $name2 $name2")
    println("multiple2: ${name}, ${name2} $name2")
    println("multiple3: ${name}, ${name2} ${name2}")
    println("$123 is not a variable, $= and $ are also not a valid name")
}