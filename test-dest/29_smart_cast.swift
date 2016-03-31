
func main(args: [String]) {
  let x: Int32? = 1
  var y: Int32 = 2
  let z: Int32? = nil

  if let x = x {
    y += x
  } else {
    print("FAIL: x should not be nil")
  }

  if let z = z {
    print("FAIL: z should be nil")
    y += z
  }

  if y != 3 {
    print("FAIL: y should be 3, is \(y)")
  }
  print("Success")
}

main([])
