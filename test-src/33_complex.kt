fun foo(a: String, b: Map<String, String>, c: ((String) -> Unit)? = null): String? {
  c?.invoke("a=$a")
  return a
}

fun fooFunc(a: String, b: Map<String, String>, c: ((String) -> Unit)? = null): String? {
  return null
}

fun run(): String {
  var outerValue = ""
  var a = foo("bar", b = HashMap<String, String>(), c = { value -> outerValue = value }) ?: return "fail1"
  a = foo("bar", b = HashMap<String, String>(), c = { value -> outerValue = value })
  fooFunc("bar", b = HashMap<String, String>(), c = { value -> outerValue = value })

  if (a != "bar") {
    println("fail: a=$a (should be bar)")
    return "fail2"
  }

  if (outerValue != "a=bar") {
    println("fail: outerValue=$outerValue (should be a=bar)")
    return "fail3"
  }

  return "ok"
}

fun main(args: Array<String>) {
  if (run() != "ok") {
    println("fail: invalid return")
  } else {
    println("success")
  }
}
