package chat

import java.util.concurrent.CountDownLatch

object ChatMain {

  def main(args: Array[String]): Unit = {

    val server = new ChatServer(8080)

    new Thread() {
      override def run(): Unit =
        server.start()
    }.start()

    val latch = new CountDownLatch(1)

    Runtime.getRuntime
      .addShutdownHook(
        new Thread() {
          override def run(): Unit = {
            println("shutdown init  ....")
            server.stop()
            println("shutdown finish  ....")
            latch.countDown()
          }
        }
      )

    latch.await()

    println("Bye Bye ....")

  }

}
