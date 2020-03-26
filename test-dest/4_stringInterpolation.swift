func x() -> String {
	return "return value"
}

func main(args: [String]) {
    let name = "Bob"
    var name2 = "Dan"
    print("Hello, \(name)!")
    print("true: \(true)")
    print("false: \(1 == 2)")
    print("function call: \(x())")
    print("my name is \(name) :)")
    print("multiple: \(name), \(name2) \(name2)")
    print("multiple2: \(name), \(name2) \(name2)")
    print("multiple3: \(name), \(name2) \(name2)")
    print("$123 is not a variable, $= and $ are also not a valid name")
}

main([])
