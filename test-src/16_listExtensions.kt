import java.util.*

fun Float.timesTen(): Float {
  return this * 10
}

fun List<Float>.avg(): Float {
  if (this.size == 0) {
    return 0f
  }

  var sum = 0.0f
  for (item in this) {
    // SWIFT: sum += item as! Double
    sum += item
  }
  return sum / this.size
}

class Person(val name: String, val age: Int) {
}

fun List<Person>.countAdults(): Int {
  var adultCounter = 0
  for (person in this) {
    if (person.age >= 18) {
      adultCounter++
    }
  }
  return adultCounter
}

fun main(args: Array<String>) {
  val list = ArrayList<Float>()
  list.add(1.5f.timesTen())
  list.add(1f.timesTen())
  list.add(11.858502f)
  list.add(3.1415f)
  val avg1 = list.avg()
  println("avg1 = $avg1 (should be 10.0)")
    
  val list2 = LinkedList<Float>()
  list2.add(15.0f)
  list2.add(5.0f)
  println("avg2 = ${list2.avg()} (should be 10.0)")

  val people = ArrayList<Person>()
  people.add(Person(name = "Steve", age = 14))
  people.add(Person(name = "Bob", age = 16))
  people.add(Person(name = "John", age = 18))
  people.add(Person(name = "Lena", age = 20))
  people.add(Person(name = "Denise", age = 22))
  people.add(Person(name = "Alex", age = 24))
  println("${people.countAdults()} people may enter the club (should be 4)")
}