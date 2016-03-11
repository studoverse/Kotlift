
func main(args: [String]) {
  // Constructors
  let hashSet0 = Set<String>()
  let linkedHashSet0 = Set<String>()
  let set0 = Set<String>()
  let set1 = Set(arrayLiteral: 1)
  let set2 = Set(arrayLiteral: 1, 2, 1)
  var hashSet = Set(arrayLiteral: 1)
  var linkedSet = Set(arrayLiteral: 2)
  var mutableSet = Set(arrayLiteral: 3)

  // Basic calls
  print("\(set1) (1)")
  print("\(set2) (1, 2)")
  print("\(set2.size) (2)")
  hashSet.add(2)
  linkedSet.addAll(linkedSet)
  mutableSet.remove(3)

  print(hashSet)
  if hashSet.size != 2 {
    print("ERROR: hashSet.size")
  }
  print(linkedSet)
  if linkedSet.size != 1 {
    print("ERROR: linkedSet.size")
  }
  print(mutableSet)
  if mutableSet.size != 0 {
    print("ERROR: mutableSet.size")
  }

  if !set0.isEmpty() {
    print("ERROR: set0.isEmpty()")
  }

  print("\(hashSet) ([2, 1])")

  // Iteration
  for k in set2 {
    print("\(k)")
  }
}

main([])
