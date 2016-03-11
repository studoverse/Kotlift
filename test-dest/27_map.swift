
func main(args: [String]) {
  // Constructors
  var hashMap0 = [String: String]()
  var linkedHashMap0 = [String: String]()
  var map0 = [String: String]()
  var map1 = [10: "hi"]
  var map2 = ["as": "hi", "df": "hello", "gh": "salut"]
  var hashMap = [1: "hi"]
  var linkedMap = [1: "hi"]
  var mutableMap = [1: "hi"]

  // Basic calls
  print("\(map1[10]) (hi)")
  print("\(map2["as"]) (hi)")
  print("\(map2.size) (3)")
  hashMap.put(2, "hello")
  linkedMap.putAll(linkedMap)
  mutableMap.remove(1)

  print(hashMap)
  if hashMap.size != 2 {
    print("ERROR: hashMap.size")
  }
  print(linkedMap)
  if linkedMap.size != 1 {
    print("ERROR: linkedMap.size")
  }
  print(mutableMap)
  if mutableMap.size != 0 {
    print("ERROR: mutableMap.size")
  }

  if !map0.isEmpty() {
    print("ERROR: map0.isEmpty()")
  }

  print("\(hashMap.keys) ([2, 1])")
  print("\(hashMap.values) ([hello, hi])")

  // Iteration
  for (k, v) in map2 {
    print("\(k) : \(v)")
  }
}

main([])
