package chat

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import org.mockito.Mockito._

import scala.util.{Failure, Success}

class ChatClientProxySpec extends WordSpec with Matchers with BeforeAndAfterEach with MockitoSugar {

  val channel1 = mock[Channel]
  val channel2 = mock[Channel]
  val channel3 = mock[Channel]

  override def beforeEach(): Unit =
    reset(channel1, channel2)

  "ChatHandler" should {

    val message = "ciao"

    "should broadcast message to other client" in {

      when(channel1.read).thenReturn(Success(message))

      val repository    = new ClientRepository()
      val client        = Client(channel1)
      val receiveClient = Client(channel2)

      repository.add(client)
      repository.add(receiveClient)

      new ChatClientProxy(client, repository).broadcastMessage()

      verify(channel1).read
      verify(channel2).write(message + "\n")
      verifyNoMoreInteractions(channel1)

    }

    "should broadcast multiple messages to other clients" in {

      when(channel1.read).thenReturn(Success(message))

      val repository     = new ClientRepository()
      val client         = Client(channel1)
      val receiveClient1 = Client(channel2)
      val receiveClient2 = Client(channel3)

      repository.add(client)
      repository.add(receiveClient1)
      repository.add(receiveClient2)

      new ChatClientProxy(client, repository).broadcastMessage()

      verify(channel1).read
      verify(channel2).write(message + "\n")
      verifyNoMoreInteractions(channel1)

    }

    "notify sender if broadcast fail" in {

      when(channel1.read).thenReturn(Failure(new Exception("error")))

      val repository = new ClientRepository()
      val client     = Client(channel1)

      new ChatClientProxy(client, repository).broadcastMessage()

      verify(channel1).write("an error occurred" + "\n")

    }

  }

}
