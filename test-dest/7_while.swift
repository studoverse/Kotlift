/**
 * `while` and `do..while` work as usual.
 * See http://kotlinlang.org/docs/reference/control-flow.html#while-loops
 */
func main(args: [String]) {
    var i = 0
    while i < args.size {
        print(args[i++])
    }
}

main([])
