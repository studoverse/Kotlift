fun main(args: Array<String>) {
  try {
    val x: Int? = 42
    val y: Int? = null

    val x1 = x ?: 0
    val y1 = y ?: 0
    val x2 = x ?: throw Exception("FAIL: Should never happen")
    val y2 = y ?: throw Exception("SUCCESS: Should happen")
    println("Fail")
  } catch (e: Exception) {
    println("Success")
  }

  val values: List<Int?> = arrayListOf(1, 2, null, null, 3, 4, null)
  for (value in values) {
    val a = value ?: continue
    println(a)
  }
}
