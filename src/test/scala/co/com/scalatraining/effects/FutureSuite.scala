package co.com.scalatraining.effects

import java.util.Random
import java.util.concurrent.Executors

import org.scalatest.FunSuite

import scala.collection.immutable.{IndexedSeq, Seq}
import scala.language.postfixOps
import scala.util.{Failure, Success}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class FutureSuite extends FunSuite {

  test("Un futuro se puede crear") {

    val hiloPpal = Thread.currentThread().getName

    var hiloFuture = ""

    println(s"Test 1 - El hilo ppal es ${hiloPpal}")

    val saludo: Future[String] = Future {
      hiloFuture = Thread.currentThread().getName
      println(s"Test 1 - El hilo del future es ${hiloFuture}")

      Thread.sleep(500)
      "Hola"
    }
    val resultado: String = Await.result(saludo, 10 seconds)
    assert(resultado == "Hola")
    assert(hiloPpal != hiloFuture)
  }

  test("map en Future") {


    val t1 = Thread.currentThread().getName
    println(s"Test 2 - El hilo del ppal es ${t1}")


    val saludo = Future {
      val t2 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del future es ${t2}")

      Thread.sleep(500)
      "Hola"
    }

    Thread.sleep(5000)

    val saludo2 = Future{
      println(s"Test 2 - Hilo normal ${Thread.currentThread().getName}")
    }

    val saludoCompleto = saludo.map(mensaje => {
      val t3 = Thread.currentThread().getName
      println(s"Test 2 - El hilo del map es ${t3}")

      mensaje + " muchachos"
    })


    val resultado = Await.result(saludoCompleto, 10 seconds)
    assert(resultado == "Hola muchachos")
  }

  test("Se debe poder encadenar Future con for-comp") {
    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2
    }

    val f3: Future[Int] = for {
      res1 <- f1
      res2 <- f2
    } yield res1 + res2

    val res = Await.result(f3, 10 seconds)

    assert(res == 3)



  }

  test("Se debe poder encadenar Future con for-comp con dividido entre 2") {
    val f1 = Future {
      Thread.sleep(200)
      1
    }

    val f2 = Future {
      Thread.sleep(200)
      2
    }

    val f3 = Future {
      Thread.sleep(200)
      2/0
    }

    val f4: Future[Int] = for {
      res1 <- f1
      res2 <- f2
      res3 <- f3.recover{case e: Exception => 0}
    } yield res1 + res2+res3

    val res = Await.result(f4, 10 seconds)

    assert(res ==3 )



  }

  test("Se debe poder manejar el error de un Future de forma imperativa") {
    val divisionCero = Future {
      Thread.sleep(100)
      10 / 0
    }
    var error = false

    val r: Unit = divisionCero.onFailure {
      case e: Exception => error = true
    }



    Thread.sleep(1000)

    assert(error == true)
  }

  test("Se debe poder manejar el exito de un Future de forma imperativa") {

    val division = Future {
      5
    }

    var r = 0

    val f: Unit = division.onComplete {
      case Success(res) => r = res
      case Failure(e) => r = 1
    }

    Thread.sleep(150)

    val res = Await.result(division, 10 seconds)

    assert(r == 5)
  }

  test("Se debe poder manejar el error de un Future de forma funcional sincronicamente") {

    var threadName1 = ""
    var threadName2 = ""

    val divisionPorCero = Future {
      threadName1 = Thread.currentThread().getName
      Thread.sleep(100)
      10 / 0
    }.recover {
      case e: ArithmeticException => {
        threadName2 = Thread.currentThread().getName
        "No es posible dividir por cero"
      }
    }

    val res = Await.result(divisionPorCero, 10 seconds)

    assert(threadName1 == threadName2)
    assert(res == "No es posible dividir por cero")

  }



  test("probando FUTURE 2") {

    def suma (n1:Int, n2:Int): Int= {
      n1+n2
    }

    val res=Future( suma(1,4))

    println(s"Aqui esta mi futuro ${res}")

  }

  test("Se debe poder manejar el error de un Future de forma funcional asincronamente") {

    var threadName1 = ""
    var threadName2 = ""

    implicit val ecParaPrimerHilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

    val f1 = Future {
      threadName1 = Thread.currentThread().getName
      2/0
    }(ecParaPrimerHilo)
    .recoverWith {
      case e: ArithmeticException => {

        implicit val ecParaRecuperacion = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

        Future{
          threadName2 = Thread.currentThread().getName
          1
        }(ecParaRecuperacion)
      }
    }

    val res = Await.result(f1, 10 seconds)

    println(s"Test en recoverWith thread del fallo: $threadName1")
    println(s"Test en recoverWith thread de recuperacion: $threadName2")

    assert(threadName1 != threadName2)
    assert(res==1)
  }

  test("Los future **iniciados** fuera de un for-comp deben iniciar al mismo tiempo") {

    val timeForf1 = 100
    val timeForf2 = 200
    val timeForf3 = 100

    val additionalTime = 50D

    val estimatedElapsed = (Math.max(Math.max(timeForf1, timeForf2), timeForf3) + additionalTime)/1000

    val f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    val f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    val f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1: Long = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **iniciados** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")
    assert(elapsed <= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future **definidos** fuera de un for-comp deben iniciar secuencialmente") {

    val timeForf1 = 100
    val timeForf2 = 300
    val timeForf3 = 500

    val estimatedElapsed:Double = (timeForf1 + timeForf2 + timeForf3)/1000

    def f1 = Future {
      Thread.sleep(timeForf1)
      1
    }
    def f2 = Future {
      Thread.sleep(timeForf2)
      2
    }
    def f3 = Future {
      Thread.sleep(timeForf3)
      3
    }

    val t1 = System.nanoTime()

    val resultado = for {
      a <- f1
      b <- f2
      c <- f3
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    println(s"Future **definidos** fuera del for-comp estimado: $estimatedElapsed real: $elapsed")

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)

  }

  test("Los future declarados dentro de un for-comp deben iniciar secuencialmente") {

    val t1 = System.nanoTime()

    val timeForf1 = 100
    val timeForf2 = 100
    val timeForf3 = 100

    val estimatedElapsed = (timeForf1 + timeForf2 + timeForf3)/1000

    val resultado = for {
      a <- Future {
        Thread.sleep(timeForf1)
        1
      }
      b <- Future {
        Thread.sleep(timeForf2)
        2
      }
      c <- Future {
        Thread.sleep(timeForf3)
        3
      }
    } yield (a+b+c)

    val res = Await.result(resultado, 10 seconds)
    val elapsed = (System.nanoTime() - t1) / 1.0E09

    assert(elapsed >= estimatedElapsed)
    assert(res == 6)
  }

  test("Future.sequence"){

    val listOfFutures: List[Future[Int]] = Range(1, 11).map(Future.successful(_)).toList

    val resSequence: Future[List[Int]] = Future.sequence {
      listOfFutures
    }

    val resFuture = resSequence.map(l => l.sum/l.size)
     println (s"${resFuture}")
    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }

  test("Future.traverse"){
    def foo(i:List[Int]):Future[Int]=Future.successful(i.sum/i.size)
    val resFuture = Future.traverse(Range(1,11).map(Future.successful(_))){
      x => x
    }.map(l => l.sum/l.size)

    val res = Await.result(resFuture, 10 seconds)

    assert(res ==  Range(1,11).sum/Range(1,11).size)

  }
  /*
   servicio de clima (5)
   2.guardar bd el valor (1)
   */


  test("Problema servicio de clima ") {

   // var threadName1 = ""
    // var threadName2 = ""
    var threadName3 = ""
    var threadName4 = ""
   //var c = ""
    //var d = ""
    var a = ""
    var b = ""

    def clima(): String = "13 C"

    def guardarValor(i: Int): String = s"Informacion guardada en la BD"


     val hiloClima = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
     val hiloGuardar = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
/*
    (1 to 10) foreach { _ =>

      val f1 = Future {
        threadName1 = Thread.currentThread().getName

        c = clima()
        println(s" Este es el HILO DE CLIMA  ${threadName1}")
        println(s" CLima  aquiii:  ${c}")
        println(s" Tiempo llamado de ejecucion ${System.nanoTime()}")
        Thread.sleep(100)

      }(hiloClima) // Se especifica el hilo que usa con currying


      val f2 = Future {
        threadName2 = Thread.currentThread().getName
        d = guardarValor(1)

        println(s" Este es el HILO DE GUARDAR  ${threadName2}")
        println(s" Resultado  aquiii:  ${d}")
        println(s" Tiempo  llamado  de ejecucion${System.nanoTime()}")
        Thread.sleep(100)
      }(hiloGuardar) // Se especifica el hilo que usa con currying
   */
      // forma dos de generar la concurrencia

      def obtenerClima() : Future[Int] = Future {
        threadName3 = Thread.currentThread().getName

        a = clima()
        println(s" Este es el HILO DE CLIMA Mejorado   ${threadName3}")
        println(s" CLima  aquiii:  ${a}")
        println(s" Tiempo llamado de ejecucion ${System.nanoTime()}")
        Thread.sleep(100)
        1

      }(hiloClima) // Se especifica el hilo que usa con currying


      def guardar(n:Int) : Future[String] = Future {
        threadName4 = Thread.currentThread().getName

        b = guardarValor(1)
        println(s" Este es el HILO DE guardar  Mejorado ${threadName4}")
        println(s" CLima  aquiii:  ${b}")
        println(s" Tiempo llamado de ejecucion ${System.nanoTime()}")
        Thread.sleep(100)
        s"Guardar : ${n}"

      }(hiloGuardar) // Se especifica el hilo que usa con currying

       val z = Future.sequence{Range(1,15).map(x=> obtenerClima().flatMap(srt => guardar(srt)))}



    }

    test("Problema Consultar repositorio"){

      var threadName: String = ""
      var threadName2: String = ""


      case class repositorio(nombre :String, lineas :String ,lenguaje :String)
      val r1 = new repositorio("damaris","10","python")
      val r2= new repositorio("juanito","35","java")
      val r3= new repositorio("andrea","22","java")
      val r4= new repositorio("julian","30","c")
      val r5= new repositorio("julian","40","java")
      val r6 = new repositorio("damaris","25","java")

      println(s"${r1}")
      println(s"${r2}")
      println(s"${r3}")
      println(s"${r4}")


      val pplhilo = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
      val pplhilo2 = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

      case class usuarioCompleto(nombre:String)



     def obtenerUsuario(usuario:String) : Future[List[repositorio]] = Future {
        threadName = Thread.currentThread().getName
       println(s"Hilo de Obtener usuario {$threadName} ")
        val lista :List[repositorio]= List(r1,r2,r3,r4,r5,r6)
        val lista2 = lista.filter(x => usuario==x.nombre)
        lista2
      }(pplhilo2)


      //def obtenerDetalle(): Future[List[repositorio]] ={



     // }



      val res = Await.result(obtenerUsuario("damaris"), 10 seconds)

      println(s"Aqui esta la respuestaaaaa {$res}")

      /*
     def obtenerRepositorio(lista: List[repositorio]):Future[repositorio]=Future{

       threadName2 = Thread.currentThread().getName
       println(s"Hilo de Obtener usuario {$threadName2} ")

       val lista2 = lista.flatMap(x => x.lenguaje== "Java")
      lista2


     }( pplhilo2)
*/



     /* def obtenerUsuarioCompleto(usuario:String) : Future[String] = Future {
        threadName2 = Thread.currentThread().getName
       ""
      }(pplhilo) // Se especifica el hilo que usa con currying*/






    }



}