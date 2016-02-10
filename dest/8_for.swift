/**
 * For loop iterates through anything that provides an iterator.
 * See http://kotlinlang.org/docs/reference/control-flow.html#for-loops
 */
func main(args: [String]) {
    for arg in args {
        print(arg)
    }

    // or
    print("")
    for i in args.indices {
        print(args[i])
    }
}

main([])
