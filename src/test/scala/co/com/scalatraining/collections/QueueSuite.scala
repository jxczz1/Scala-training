package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Queue

class QueueSuite extends FunSuite{

  test("Construccion de un  Cola"){
    val cola = Queue(1, 2, 3)

    assert(3==cola.size)
  }

  test("Agregar elementos a una cola"){
    val cola = Queue(1, 2, 3)
    val cola2 = cola.enqueue(4)
    assert(Queue(1,2,3,4)==cola2)
  }





}
