/**
 * Check if a number lies within a range.
 * Check if a number is out of range.
 * Check if a collection contains an object.
 * See http://kotlinlang.org/docs/reference/ranges.html#ranges
 */

fun main(args: Array<String>) {
    var x = 0
    try {
        x = Integer.parseInt(args[0])
    } catch (ignore: NumberFormatException) {
    }
    //Check if a number lies within a range:
    val y = 10
    if (x in 1..y - 1) {
        println("OK")
    }

    //Iterate over a range:
    for (a in 1..5) {
        print("${a} ")
    }

    //Check if a number is out of range:
    println()
    var array = arrayListOf<String>()
    array.add("aaa")
    array.add("bbb")
    array.add("ccc")
}
