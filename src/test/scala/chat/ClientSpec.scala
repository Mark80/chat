package chat

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class ClientSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  "Client" should {

    "write on channel" in {

      val server = new ServerSocket(8080)

      val telnetSocket = new Socket("localhost", 8080)

      val serverSocket: Socket = server.accept()

      val client = Client(new SocketChannel(serverSocket))

      val bufferedReader = new BufferedReader(new InputStreamReader(telnetSocket.getInputStream))

      client.write("message\n")

      val msg = bufferedReader.readLine()

      msg shouldBe "message"

      server.close()
      client.close()
      bufferedReader.close()
    }

    "read on channel" in {

      val server = new ServerSocket(8080)

      val telnetSocket = new Socket("localhost", 8080)

      val serverSocket: Socket = server.accept()

      val client = Client(new SocketChannel(serverSocket))

      telnetSocket.getOutputStream.write("message\n".getBytes)

      val msg = client.read()

      msg.get shouldBe "message"

      server.close()
      client.close()
    }

  }

}
