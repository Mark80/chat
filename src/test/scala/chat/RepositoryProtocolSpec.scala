package chat

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class RepositoryProtocolSpec extends WordSpec with Matchers with BeforeAndAfterEach with MockitoSugar {

  val channel = mock[Channel]

  "ClientRepository" should {

    val client  = Client(channel)
    val client2 = Client(channel)
    val client3 = Client(channel)

    "return the client added" in {

      val repository = new ClientRepository

      repository.add(client)
      repository.add(client2)

      repository.getAll should contain theSameElementsAs List(client, client2)

    }

    "not add the same client multiple times" in {

      val repository = new ClientRepository

      repository.add(client)
      repository.add(client)
      repository.add(client)

      repository.getAll should contain theSameElementsAs List(client)

    }

    "return all client to broadcast" in {

      val repository = new ClientRepository

      repository.add(client)
      repository.add(client2)
      repository.add(client3)

      repository.getAllOtherClient(client.id) should contain theSameElementsAs List(client2, client3)

    }

    "remove a client" in {

      val repository = new ClientRepository

      repository.add(client)
      repository.add(client2)
      repository.add(client3)

      repository.remove(client.id)

      repository.getAll should contain theSameElementsAs List(client2, client3)

    }

  }

}
