package chat

import java.io.{BufferedReader, InputStreamReader}
import java.net.{InetSocketAddress, Socket}
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class ChatSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  "ChatServer" should {

    "accept client and broadcast messages" in {

      val chat = new ChatServer(8080)
      new Thread(() => chat.start()).start()

      val client1 = new Socket
      client1.connect(new InetSocketAddress("localhost", 8080))

      val client2 = new Socket
      client2.connect(new InetSocketAddress("localhost", 8080))

      val client3 = new Socket
      client3.connect(new InetSocketAddress("localhost", 8080))

      waitForOneSecond()

      val buf2 = bufferReader(client2)
      val buf3 = bufferReader(client3)

      val out = client1.getOutputStream
      out.write("pippo\n".getBytes)

      buf2.readLine() shouldBe "pippo"

      buf3.readLine() shouldBe "pippo"

      println(">>>>>>>>>>>>>>")

      chat.stop()

    }

  }

  private def waitForOneSecond(): Unit =
    Thread.sleep(1000)

  private def bufferReader(client: Socket) =
    new BufferedReader(new InputStreamReader(client.getInputStream))
}
