
extension Double {
func timesTen() -> Double {
  return self * 10
}
}

extension Array where Element : Addable {
func avg() -> Double {
  if self.size == 0 {
    return 0
  }

  var sum = 0.0
  for item in self {
    sum += item as! Double
  }
  return sum / self.size
}
}

public class Person {
  let name: String
  let age: Int32

  init(name: String, age: Int32) {
    self.name = name
    self.age = age
  }
}

extension Array where Element : Person {
func countAdults() -> Int32 {
  var adultCounter = 0
  for person in self {
    if person.age >= 18 {
      adultCounter++
    }
  }
  return adultCounter
}
}

func main(args: [String]) {
  var list = [Double]()
  list.add(1.5.timesTen())
  list.add(1.timesTen())
  list.add(11.858502)
  list.add(3.1415)
  let avg1 = list.avg()
  print("avg1: \(avg1) (should be 10.0)")

  var list2 = [Double]()
  list2.add(15.0)
  list2.add(5.0)
  print("avg2: \(list2.avg()) (should be 10.0)")

  var people = [Person]()
  people.add(Person(name: "Steve", age: 14))
  people.add(Person(name: "Bob", age: 16))
  people.add(Person(name: "John", age: 18))
  people.add(Person(name: "Lena", age: 20))
  people.add(Person(name: "Denise", age: 22))
  people.add(Person(name: "Alex", age: 24))
  print("\(people.countAdults()) people may enter the club (should be 4)")
}

main([])
