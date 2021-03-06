package co.com.scalatraining.scalatest.suites

import org.scalatest.FunSuite

class SuiteExample extends FunSuite {

  test("An empty Set should have size 0") {
    assert(Set.empty.size == 1)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }






  }
}