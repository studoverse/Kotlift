
func foo() throws {
  throw java.lang.IllegalStateException("Do not call foo")
}

func bar() throws {
  throw java.lang.IllegalStateException("Do not call bar")
}

func fooReturns() throws -> String {
  throw java.lang.IllegalStateException("Do not call fooReturns")
}

func barReturns() throws -> Int32 {
  throw java.lang.IllegalStateException("Do not call bar")
}

func main(args: [String]) {
  do {
    try foo()
    try bar()
    var x = try fooReturns()
    let y = try barReturns()
  } catch {
    print("Success")
  }
}

main([])
