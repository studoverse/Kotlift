// See https://kotlinlang.org/docs/reference/null-safety.html

class Node(val parentNode: Node?, val nodeName: String?) {
  fun getParent(): Node? {
    return parentNode
  }
  fun getName(): String? {
    return nodeName
  }
}

fun foo(node: Node): String? {
  val parent = node.getParent() ?: return null
  val name = node.getName() ?: "THROW IS CURRENTLY NOT SUPPORTED" //throw IllegalArgumentException("name expected")

  return "foo returns $name"
}

fun main(args: Array<String>) {
  // Test simple elvis operator
  val b: String? = "asdf"
  // SWIFT: let c = b != nil ? b!.length : -1
  val c = if (b != null) b.length else -1
  val d = b?.length ?: -1
  val error = if (c != 4 || d != 4) "ERROR" else "OK"
  println("$error: c=$c (should be 4), d=$d (should be 4)")

  // Elvis return test
  val node1 = Node(parentNode = null, nodeName = "node1Name")
  val node2 = Node(parentNode = node1, nodeName = "node2Name")
  println("$node1: ${node1.getParent()} - ${node1.getName()}")
  println("$node2: ${node2.getParent()} - ${node2.getName()}")
  if (foo(node1) != null) {
    println("Error1")
  }
  if (foo(node2) != "foo returns node2Name") {
    println("Error2")
  }

  // Test throw
  val node3 = Node(parentNode = node2, nodeName = null)
  println("$node3: ${node3.getParent()} - ${node3.getName()}")
  println(foo(node3))
}
