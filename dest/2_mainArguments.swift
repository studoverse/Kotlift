/**
 * Line 13 demonstrates string templates and array access.
 * See this pages for details:
 * http://kotlinlang.org/docs/reference/basic-types.html#strings
 * http://kotlinlang.org/docs/reference/basic-types.html#arrays
 */

func main(args: [String]) {
    if args.size == 0 {
        print("Please provide a name as a command-line argument")
        return
    }
    print("Hello, \(args[0])!")
}

main([])
