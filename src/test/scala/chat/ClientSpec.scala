package chat

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class ClientSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  private var server: ServerSocket = _

  override def beforeEach(): Unit =
    server = new ServerSocket(8080)

  "Client" should {

    "write on channel" in {

      val telnetSocket = new Socket("localhost", 8080)

      val serverSocket: Socket = server.accept()

      val client = Client(new SocketChannel(serverSocket))

      val bufferedReader = readFrom(telnetSocket)

      client.write("message\n")

      val msg = bufferedReader.readLine()

      msg shouldBe "message"

      client.close()
      telnetSocket.close()
    }

    "read on channel" in {

      val telnetSocket = new Socket("localhost", 8080)

      val serverSocket: Socket = server.accept()

      val client = Client(new SocketChannel(serverSocket))

      telnetSocket.getOutputStream.write("message\n".getBytes)

      val msg = client.read()

      msg.get shouldBe "message"

      client.close()
      telnetSocket.close()
    }

    "close channel" in {

      new Socket("localhost", 8080)

      val clientSocket: Socket = server.accept()

      val channel = new SocketChannel(clientSocket)
      val client  = Client(channel)

      client.close()
      clientSocket.isClosed shouldBe true
    }

  }

  private def readFrom(telnetSocket: Socket) =
    new BufferedReader(new InputStreamReader(telnetSocket.getInputStream))

  override def afterEach(): Unit =
    server.close()

}
