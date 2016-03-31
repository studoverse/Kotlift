/* Output:
[1, 2, 3, 4, 10, 0]
[2, 4, 6, 8, 20, 0]
[2, 4, 6, 8, 20, 0]
10
20
["1€", "2€", "3€", "4€", "10€", "0€"]
["1€", "2€", "3€", "4€", "10€", "0€"]
1234100
*/

// SWIFT: extension Array {

// SWIFT: public func myMap<R>(transform: (Element) -> R) -> Array<R> {
fun <T, R> List<T>.myMap(transform: (T) -> R): List<R> {
  val result = arrayListOf<R>()
  for (item in this) {
    result.add(transform(item))
  }
  return result
// SWIFT: }

}

fun <T> myMax(collection: Collection<T>, less: (T, T) -> Boolean): T? {
  var max: T? = null
  for (it in collection) {
    // SWIFT: if (max == nil || less(max!, it)) {
    if (max == null || less(max, it)) {
      max = it
    }
  }
  return max
}

fun main(args: Array<String>) {
  val ints = arrayListOf(1, 2, 3, 4, 10, 0)
  val ints2 = arrayListOf(1, 2, 3, 4, 10, 0)
  val doubled1 = ints.myMap { element -> element * 2 }
  val doubled2 = ints.myMap { it * 2 }

  println(ints)
  println(doubled1)
  println(doubled2)

  println(myMax(ints2, less = {a, b -> a < b}))
  println(myMax(doubled1, less = {a, b -> a < b}))

  // Use stdlib
  println(ints.map { "$it€"} )
  println(ints.map({"$it€"}))

  // With parameter
  ints.forEach { intVal ->
    print(intVal)
  }
}
