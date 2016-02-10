class Box<T> {
  var value: T // value = t won't work, as self must happen in swift inside constructor

  func doNothing(value2: T) -> T {
    return value
  }

  init(t: T) {
    value = t
  }
}

class BigBox<T> {
  var value1: T
  let value2: T

  init(t: T) {
    value1 = t
    value2 = t
  }
}

func main(args: [String]) {
  let box: Box<Int> = Box<Int>(t: 1)
  print(box)

  let box2 = Box(t: 2)
  print(box2)

  print(box.doNothing(4))

  let bigBox = BigBox(t: 3)
  print(bigBox.value1 + bigBox.value2)
}

main([])
