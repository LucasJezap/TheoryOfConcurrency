package producer_consumer

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Stash}
import akka.event.LoggingReceive

object PC {

  case class Init()

  case class Put(x: Long)

  case class Get()

  case class ProduceDone()

  case class ConsumeDone(x: Long)

}

class Producer(name: String, buf: ActorRef, maxLongValue: Long) extends Actor {

  import PC._

  def receive: Receive = LoggingReceive {
    case Init =>
      buf ! Put((Math.random() * maxLongValue).longValue())
    case ProduceDone =>
      buf ! Put((Math.random() * maxLongValue).longValue())

  }

}

class Consumer(name: String, buf: ActorRef) extends Actor {

  import PC._

  def receive: Receive = LoggingReceive {
    case Init =>
      buf ! Get
    case ConsumeDone(x) =>
      buf ! Get
  }

}


class Buffer(n: Int, showBufferInfo: Boolean) extends Actor with Stash {

  import PC._

  val buffer = new Array[Long](n)
  var takenPlaces = 0

  // sender is the default name
  // FIFO buffer
  def receive: Receive = LoggingReceive {
    case Put(x) if takenPlaces < n =>
      sender ! ProduceDone
      buffer(takenPlaces) = x
      takenPlaces += 1
      if (showBufferInfo) println(s"Buffer after Put($x) from ${sender.path.name}: ${buffer.mkString("Array(", ", ", ")")}")
      unstashAll(); // unstash all Put()
    case Get if takenPlaces > 0 =>
      val value = buffer(takenPlaces - 1)
      sender ! ConsumeDone(value)
      buffer(takenPlaces - 1) = 0
      takenPlaces -= 1
      if (showBufferInfo) println(s"Buffer after Get($value) from ${sender.path.name}: ${buffer.mkString("Array(", ", ", ")")}")
      unstashAll(); // unstash all Get()
    case x =>
      println(s"Stashing $x from ${sender.path.name}")
      stash()
  }
}


object ProdConsMain extends App {

  import PC._

  private val system = ActorSystem("producer_consumer")
  private val maxLongValue = 100000.longValue()
  private val showBufferInfo = true
  private val bufferSize = 3
  private val numberOfProducers = 5
  private val numberOfConsumers = 5
  private val sleepTimeMillis = 10

  val buffer = system.actorOf(Props(new Buffer(bufferSize, showBufferInfo)), "buffer")
  for (i <- 1 to numberOfProducers) {
    val producer = system.actorOf(Props(new Producer(s"Producer $i", buffer, maxLongValue)), s"Producer$i")
    producer ! Init
  }
  for (i <- 1 to numberOfConsumers) {
    val consumer = system.actorOf(Props(new Consumer(s"Consumer $i", buffer)), s"Consumer$i")
    consumer ! Init
  }

  Thread.sleep(sleepTimeMillis)
  system.terminate()
}


