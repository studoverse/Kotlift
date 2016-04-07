func main(args: [String]) {
  do {
    let x: Int32? = try 42
    let y: Int32? = try nil

    let x1 = try x ?? 0
    let y1 = try y ?? 0
    let x2 = try x ?! { throw Exception("FAIL: Should never happen") }
    let y2 = try y ?! { throw Exception("SUCCESS: Should happen") }
    try print("Fail")
  } catch {
    print("Success")
  }

  var values: Array<Int32?> = [1, 2, nil, nil, 3, 4, nil]
  for value in values {
    let _kotliftOptional1 = value; if _kotliftOptional1 == nil { continue }; let a = _kotliftOptional1!
    print(a)
  }
}

main([])
