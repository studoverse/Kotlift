import java.util.*

fun main(args: Array<String>) {
  // Constructors
  val hashMap0 = HashMap<String, String>()
  val linkedHashMap0 = LinkedHashMap<String, String>()
  val map0 = emptyMap<String, String>()
  val map1 = mapOf(Pair(10, "hi"))
  val map2 = mapOf(Pair("as", "hi"), Pair("df", "hello"), Pair("gh", "salut"))
  var hashMap = hashMapOf(Pair(1, "hi"))
  var linkedMap = linkedMapOf(Pair(1, "hi"))
  var mutableMap = mutableMapOf(Pair(1, "hi"))

  // Basic calls
  println("${map1[10]} (hi)")
  println("${map2["as"]} (hi)")
  println("${map2.size} (3)")
  hashMap.put(2, "hello")
  linkedMap.putAll(linkedMap)
  mutableMap.remove(1)

  println(hashMap)
  if (hashMap.size != 2) {
    println("ERROR: hashMap.size")
  }
  println(linkedMap)
  if (linkedMap.size != 1) {
    println("ERROR: linkedMap.size")
  }
  println(mutableMap)
  if (mutableMap.size != 0) {
    println("ERROR: mutableMap.size")
  }

  if (!map0.isEmpty()) {
    println("ERROR: map0.isEmpty()")
  }

  println("${hashMap.keys} ([2, 1])")
  println("${hashMap.values} ([hello, hi])")

  // Iteration
  for ((k, v) in map2) {
    println("$k : $v")
  }
}
