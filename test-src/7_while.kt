/**
 * `while` and `do..while` work as usual.
 * See http://kotlinlang.org/docs/reference/control-flow.html#while-loops
 */
fun main(args: Array<String>) {
    var i = 0
    while (i < args.size) {
        println(args[i++])
    }
}