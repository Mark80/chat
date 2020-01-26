package chat

import java.util.concurrent.atomic.AtomicBoolean

import scala.util.Try

class ClientHandler(client: Client, repository: ClientRepository) extends Thread {

  private val controller = new AtomicBoolean(true)

  override final def run(): Unit =
    while (controller.get()) {
      broadcastMessage()
    }

  def broadcastMessage(): Unit = {
    val tryReadMessage = readMessage

    tryReadMessage.map { message =>
      if (clientIsAlive(message))
        broadcast(message)
      else
        closeClient()
    }.recover {
      case _: Throwable => client.write("an error occurred")
    }

    ()
  }

  private def closeClient(): Unit = {
    client.close()
    controller.set(false)
    repository.remove(client.id)
  }

  private def broadcast(message: String): Unit =
    repository
      .getAllOtherClient(client.id)
      .foreach(cli => {
        cli.write(message)
      })

  private def readMessage: Try[String] =
    client.read()

  private def clientIsAlive(msg: String): Boolean =
    msg != null
}
