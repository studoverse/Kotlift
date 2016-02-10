/**
  * The `is` operator checks if an expression is an instance of a type and more.
  * If we is-checked an immutable local variable or property, there's no need
  * to cast it explicitly to the is-checked type.
  * See this pages for details:
  * http://kotlinlang.org/docs/reference/classes.html#classes-and-inheritance
  * http://kotlinlang.org/docs/reference/typecasts.html#smart-casts
 */
fun main(args: Array<String>) {
  println(getStringLength("aaa"))
  println(getStringLength(1))
}

fun getStringLength(obj: Any): Int? {
  if (obj is String) {
    // SWIFT: return (obj as! String).length // no cast to String is needed
    return obj.length // no cast to String is needed
  }
  return null
}