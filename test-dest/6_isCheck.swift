/**
  * The `is` operator checks if an expression is an instance of a type and more.
  * If we is-checked an immutable local variable or property, there's no need
  * to cast it explicitly to the is-checked type.
  * See this pages for details:
  * http://kotlinlang.org/docs/reference/classes.html#classes-and-inheritance
  * http://kotlinlang.org/docs/reference/typecasts.html#smart-casts
 */
func main(args: [String]) {
  print(getStringLength("aaa"))
  print(getStringLength(1))
}

func getStringLength(obj: Any) -> Int32? {
  if obj is String {
    return (obj as! String).length // no cast to String is needed
  }
  return nil
}

main([])
