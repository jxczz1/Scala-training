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

  test("obtenerelprimerElemento"){
    val cola = List(1,2,3,4)



  }

  test (testName = "Transformacion de un Queue map"){

    val cola = Queue(1,2,3,4,5)
    assert(Queue(2,4,6,8,10) == cola.map(x => x*2))

  }


  test (testName = "Filtrar una cola "){

    val cola = Queue("juanito","Damaris","julio","andres")
    assert(Queue("juanito","Damaris","andres") ==cola.filter(x=> x!="julio"))




  }


  test (testName = "Sumar en una cola  ") {

    val cola = Queue(1, 2, 3, 4, 5, 6, 7, 8, 9)

    assertResult(45) {
      cola.sum


    }
  }

    test (testName = "ver los elementos en cola despues del primero "){

      val cola = Queue(1,2,3,4,5,6,7,8,9)

      assertResult(Queue(2,3,4,5,6,7,8,9)){
       cola.tail

      }
    }


 


  test (testName = "Selecciona los ultimos n elementos"){

    val cola = Queue(1,2,3,4,5,6,7,8,9)

    assertResult(Queue(5,6,7,8,9)){
      cola.takeRight(5)

    }
  }



  test (testName = "Union de dos colas"){

    val cola = Queue(1,2,3,4,5,6,7,8,9)
    val cola2 = Queue(1,2,3,4,5,6,7,8,9)

    assertResult(Queue(1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9)){
       cola.union(cola2)

    }
  }


  test (testName = "Obtener el ultimo  "){

    val cola = Queue(1,2,3,4,5,6,7,8,9)
    val cola2 = Queue(1,2,3,4,5,6,7,8,10)

    assertResult( Queue(9)){
      Queue(9)

    }


    }










}
