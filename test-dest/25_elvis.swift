// See https://kotlinlang.org/docs/reference/nil-safety.html

public class Node {
  let parentNode: Node?
  let nodeName: String?

  func getParent() -> Node? {
    return parentNode
  }
  func getName() -> String? {
    return nodeName
  }
  init(parentNode: Node?, nodeName: String?) {
    self.parentNode = parentNode
    self.nodeName = nodeName
  }
}

func foo(node: Node) -> String? {
  let parent = node.getParent() ?? nil
  if parent == nil { return nil } // Return from elvis operator
  let name = node.getName() ?? "THROW IS CURRENTLY NOT SUPPORTED" //throw IllegalArgumentException("name expected")

  return "foo returns \(name)"
}

func main(args: [String]) {
  // Test simple elvis operator
  let b: String? = "asdf"
  let c = b != nil ? b!.length : -1
  let d = b?.length ?? -1
  let error = c != 4 || d != 4 ? "ERROR" : "OK"
  print("\(error): c=\(c) (should be 4), d=\(d) (should be 4)")

  // Elvis return test
  let node1 = Node(parentNode: nil, nodeName: "node1Name")
  let node2 = Node(parentNode: node1, nodeName: "node2Name")
  print("\(node1): \(node1.getParent()) - \(node1.getName())")
  print("\(node2): \(node2.getParent()) - \(node2.getName())")
  if foo(node1) != nil {
    print("Error1")
  }
  if foo(node2) != "foo returns node2Name" {
    print("Error2")
  }

  // Test throw
  let node3 = Node(parentNode: node2, nodeName: nil)
  print("\(node3): \(node3.getParent()) - \(node3.getName())")
  print(foo(node3))
}

main([])
