/**
 * Line 2 demonstrates the for-loop, that would have been called "enhanced"
 * if there were any other for-loop in Kotlin.
 * See http://kotlinlang.org/docs/reference/basic-syntax.html#using-a-for-loop
 */

func main(args: [String]) {
    for name in args {
        print("Hello, \(name)!")
    }
}

main([])
