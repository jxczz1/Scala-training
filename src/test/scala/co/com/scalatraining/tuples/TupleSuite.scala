package co.com.scalatraining.tuples

import org.scalatest.FunSuite

class TupleSuite  extends FunSuite {

  test("Una tupla se debe poder crear"){
    val tupla = (1, 2,"3", List(1, 2, 3))
    assert(tupla._2 == 2)
    assert(tupla._4.tail.head == 2)
  }

  test("una tupla de 5 lista"){
    val tupla = (List(2,4,5), List(1, 2, 3),List(8,9,10),List(11,12,13),List(14,15,16))

    val tupla2 =(tupla._1.head,tupla._2.head,tupla._3.head,tupla._4.head,tupla._5.head)

    assert(tupla2 == (2,1,8,11,14))

  }

  test("test obtenga el promedio de los numeros pares"){


  }


}
