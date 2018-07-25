package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Stack


class StackSuite extends FunSuite{


  test("Construccion de un  pila"){
    val pila = Stack(1,2,3)
    assert(pila.size==3)
  }

  test("Agregar datos a una pila"){

    val nombres = Stack[String]()

    val nombres2 = nombres.push("juanito")

    assert(Stack("juanito")==nombres2)


  }

  test("sacar datos de una pila"){

    val nombres = Stack[String]("juanito","ana","damaris")
    val pop = nombres.pop
    println("aquiii"+pop)
    val result=Stack("ana","damaris")

    assert(result==pop)


  }


  test("ver el siguiente elemento en la pila sin eliminarlo"){

    val nombres = Stack[String]("juanito","ana","damaris")

    val top = nombres.top

    assert("juanito"==top)



  }


  test("ver si una pila esta vacia "){

    val nombres = Stack[String]("juanito","ana","damaris")

    val estavacia = nombres.isEmpty

    assert(false==estavacia)



  }

  test("Comparar si dos pilas  son iguales "){

    val nombres = Stack[String]("juanito","ana","damaris")

    val estavacia = nombres.isEmpty

    assert(false==estavacia)


  }


  test(" invertir la pila "){

    val pilaNums = Stack[Int](1,2,3,4,5)

    val pilaInvertida = pilaNums.reverse

    assert(Stack(5,4,3,2,1)==pilaInvertida)


  }



}
