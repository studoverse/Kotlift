class Lock() {
  fun lock() {
    println("  locked")
  }
  fun unlock() {
    println("  unlocked")
  }
}

fun <T> lock(lock: Lock, body: () -> T): T {
  lock.lock()
  val x = body()
  lock.unlock()
  return x
}

fun main(args: Array<String>) {
  val lockObj = Lock()

  // Simple lock
  println("before lock")
  lock(lockObj) {
    println("    currently locked")
  }
  println("after lock")
}
