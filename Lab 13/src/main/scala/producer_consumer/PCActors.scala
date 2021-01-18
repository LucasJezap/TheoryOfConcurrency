package producer_consumer

// Created by Lukasz Jezapkowicz

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

  // here's action configuration for producer when he receives message
  // Init - produce first value and sends it to buffer
  // ProduceDone - produce subsequent value and sends it to buffer
  // the producer simply messages buffer with appropriate function Put()
  // all info is printed in buffer
  def receive: Receive = LoggingReceive {
    case Init =>
      buf ! Put((Math.random() * maxLongValue).longValue())
    case ProduceDone =>
      buf ! Put((Math.random() * maxLongValue).longValue())

  }

}

class Consumer(name: String, buf: ActorRef) extends Actor {

  import PC._

  // here's action configuration for consumer when he receives message
  // Init - try to get first value from buffer
  // ProduceDone - try to get subsequent value from buffer
  // the consumer simply messages buffer with appropriate function Get()
  // all info is printed in buffer
  def receive: Receive = LoggingReceive {
    case Init =>
      buf ! Get
    case ConsumeDone(x) =>
      buf ! Get
  }

}


class Buffer(n: Int, showBufferInfo: Boolean) extends Actor with Stash {

  import PC._

  // buffer of n Long values (simple array)
  // the buffer is a FIFO object (like a stack)
  // we take record of last element in buffer with takenPlaces variable
  val buffer = new Array[Long](n)
  var takenPlaces = 0

  // here's action configuration for buffer when it receives message
  // Put - puts new element in the array if there's a free place for it
  // Get - reads an element from the array if there's any in it
  // _ - if none of above occurs then we stash the message - when we get
  // the opportunity to either Put or Get we will unstash all messages and
  // try to put/read them right then
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
      // we zero the appropriate buffer cell
      buffer(takenPlaces - 1) = 0
      takenPlaces -= 1
      if (showBufferInfo) println(s"Buffer after Get($value) from ${sender.path.name}: ${buffer.mkString("Array(", ", ", ")")}")
      unstashAll(); // unstash all Get()
    case x =>
      // we stash the operation (Put or Get)
      println(s"Stashing $x from ${sender.path.name}")
      stash()
  }
}


object ProdConsMain extends App {

  import PC._

  // configuration variables such as buffer size or number of producers/consumers
  private val system = ActorSystem("producer_consumer")
  private val maxLongValue = 100000.longValue()
  private val showBufferInfo = true
  private val bufferSize = 3
  private val numberOfProducers = 5
  private val numberOfConsumers = 5
  private val sleepTimeMillis = 10

  // creating buffer as an actor using Props configuration object
  val buffer = system.actorOf(Props(new Buffer(bufferSize, showBufferInfo)), "buffer")
  // creating appropriate number of producers
  for (i <- 1 to numberOfProducers) {
    val producer = system.actorOf(Props(new Producer(s"Producer $i", buffer, maxLongValue)), s"Producer$i")
    producer ! Init
  }
  // creating appropriate number of consumers
  for (i <- 1 to numberOfConsumers) {
    val consumer = system.actorOf(Props(new Consumer(s"Consumer $i", buffer)), s"Consumer$i")
    consumer ! Init
  }

  // main threads sleeps for some time and then terminates
  Thread.sleep(sleepTimeMillis)
  system.terminate()
}

/* example output (buffer size = 3, 5 producers and consumers):
23:16:45 INFO  Slf4jLogger {akka.event.slf4j.Slf4jLogger$$anonfun$receive$1 applyOrElse} - Slf4jLogger started
Buffer after Put(66734) from Producer4: Array(66734, 0, 0)
Buffer after Put(8100) from Producer2: Array(66734, 8100, 0)
Buffer after Put(65602) from Producer1: Array(66734, 8100, 65602)
Stashing Put(12219) from Producer5
Stashing Put(49359) from Producer3
Buffer after Get(65602) from Consumer2: Array(66734, 8100, 0)
Buffer after Put(12219) from Producer5: Array(66734, 8100, 12219)
Stashing Put(49359) from Producer3
Buffer after Get(12219) from Consumer4: Array(66734, 8100, 0)
Buffer after Put(49359) from Producer3: Array(66734, 8100, 49359)
Buffer after Get(49359) from Consumer5: Array(66734, 8100, 0)
Buffer after Get(8100) from Consumer3: Array(66734, 0, 0)
Buffer after Get(66734) from Consumer1: Array(0, 0, 0)
Buffer after Put(12689) from Producer4: Array(12689, 0, 0)
...
...
...
*/

