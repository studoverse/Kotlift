func foo(a: String, b: [String: String], c: ((String) -> Void)? = nil) -> String? {
  c?("a=\(a)")
  return a
}

func fooFunc(a: String, b: [String: String], c: ((String) -> Void)? = nil) -> String? {
  return nil
}

func run() -> String {
  var outerValue = ""
  let _kotliftOptional1 = foo("bar", b: [String: String](), c: { value in outerValue: value }); if _kotliftOptional1 == nil { return "fail1" }; var a = _kotliftOptional1!
  a = foo("bar", b: [String: String](), c: { value in outerValue: value })
  fooFunc("bar", b: [String: String](), c: { value in outerValue: value })

  if a != "bar" {
    print("fail: a=\(a) (should be bar)")
    return "fail2"
  }

  if outerValue != "a=bar" {
    print("fail: outerValue=\(outerValue) (should be a=bar)")
    return "fail3"
  }

  return "ok"
}

func main(args: [String]) {
  if run() != "ok" {
    print("fail: invalid return")
  } else {
    print("success")
  }
}

main([])
