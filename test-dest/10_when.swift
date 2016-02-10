/**
 * See https://kotlinlang.org/docs/reference/control-flow.html
 */

func main(args: [String]) {
    cases(1)
    cases(2)
    cases(3)
    cases(4)
}

func cases(x: Int) {
    switch x {
        case 1: print("x == 1")
        case 2: print("x == 2")
        default: // Note the block
            print("x is neither 1 nor 2")
            print("x might be something more")
    }

    switch x {
        case 0, 1: print("x == 0 or x == 1")
        default: print("otherwise")
    }
}

main([])
