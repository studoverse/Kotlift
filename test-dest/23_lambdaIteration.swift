/* Output:
[1, 2, 3, 4, 10, 0]
[2, 4, 6, 8, 20, 0]
[2, 4, 6, 8, 20, 0]
10
20
["1€", "2€", "3€", "4€", "10€", "0€"]
["1€", "2€", "3€", "4€", "10€", "0€"]
*/

extension Array {
public func myMap<R>(transform: (Element) -> R) -> Array<R> {
  var result = [R]()
  for item in self {
    result.add(transform(item))
  }
  return result
}
}

func myMax<T>(collection: Array<T>, less: (T, T) -> Bool) -> T? {
  var max: T? = nil
  for it in collection {
    if (max == nil || less(max!, it)) {
      max = it
    }
  }
  return max
}

func main(args: [String]) {
  let ints = [1, 2, 3, 4, 10, 0]
  let ints2 = [1, 2, 3, 4, 10, 0]
  let doubled1 = ints.myMap { element in element * 2 }
  let doubled2 = ints.myMap { $0 * 2 }

  print(ints)
  print(doubled1)
  print(doubled2)

  print(myMax(ints2, less: {a, b in a < b}))
  print(myMax(doubled1, less: {a, b in a < b}))

  // Use stdlib
  print(ints.map { "\($0)€"} )
  print(ints.map({"\($0)€"}))
}

main([])
