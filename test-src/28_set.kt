import java.util.*

fun main(args: Array<String>) {
  // Constructors
  val hashSet0 = HashSet<String>()
  val linkedHashSet0 = LinkedHashSet<String>()
  val set0 = emptySet<String>()
  val set1 = setOf(1)
  val set2 = setOf(1, 2, 1)
  var hashSet = hashSetOf(1)
  var linkedSet = linkedSetOf(2)
  var mutableSet = mutableSetOf(3)

  // Basic calls
  println("${set1} (1)")
  println("${set2} (1, 2)")
  println("${set2.size} (2)")
  hashSet.add(2)
  linkedSet.addAll(linkedSet)
  mutableSet.remove(3)

  println(hashSet)
  if (hashSet.size != 2) {
    println("ERROR: hashSet.size")
  }
  println(linkedSet)
  if (linkedSet.size != 1) {
    println("ERROR: linkedSet.size")
  }
  println(mutableSet)
  if (mutableSet.size != 0) {
    println("ERROR: mutableSet.size")
  }

  if (!set0.isEmpty()) {
    println("ERROR: set0.isEmpty()")
  }

  println("${hashSet} ([2, 1])")

  // Iteration
  for (k in set2) {
    println("$k")
  }
}
