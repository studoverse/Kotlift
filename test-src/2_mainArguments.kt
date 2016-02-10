/**
 * Line 13 demonstrates string templates and array access.
 * See this pages for details:
 * http://kotlinlang.org/docs/reference/basic-types.html#strings
 * http://kotlinlang.org/docs/reference/basic-types.html#arrays
 */

fun main(args: Array<String>) {
    if (args.size == 0) {
        println("Please provide a name as a command-line argument")
        return
    }
    println("Hello, ${args[0]}!")
}