/**
 * For loop iterates through anything that provides an iterator.
 * See http://kotlinlang.org/docs/reference/control-flow.html#for-loops
 */
fun main(args: Array<String>) {
    for (arg in args) {
        println(arg)
    }

    // or
    println()
    for (i in args.indices) {
        println(args[i])
    }
}