package co.com.scalatraining.effects

import org.scalatest.FunSuite

import scala.collection.immutable.Seq

class OptionSuite extends FunSuite {

  test("Se debe poder crear un Option con valor"){
    val s = Option{
      1// idea es de que vaya una expresion
    }
    assert(s == Some(1))
  }

  test("Se debe poder crear un con valor Some"){
    val s = Some{
      1// idea es de que vaya una expresion
    }
    assert(s == Some(1))
  }

  test("test con Some null"){
    val s = Some{
      null// idea es de que vaya una expresion
    }
    assert(s == Some(null))
  }

  test("test con option null"){
    val s = Option{
      null// idea es de que vaya una expresion
    }
    assert(s == None)
  }


  test("Se debe poder crear un Option para denotar que no hay valor"){
    val s = None
    assert(s == None)
  }

  test("Es inseguro acceder al valor de un Option con get"){
    val s = None
    assertThrows[NoSuchElementException]{
      val r = s.get
    }


  }

  test("Se debe poder hacer pattern match sobre un Option") {
    val lista: Seq[Option[String]] = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre: Option[String] = lista(1)
    var res = ""
    res = nombre match { //pattern match como un caso
      case Some(nom) => nom
      case None => "NONAME"
    }
    assert(res == "NONAME")
  }

  test("Fold en Option"){
    val o = Option(1)

    val res: Int = o.fold{// en caso fold
      10 // primera lambda
    }{// en caso Some
      x => x + 20 //segunda lambda
    }

    assert(res == 21)
  }

  test("Fold en Option de null si es par o no es par"){

  }



  test("Se debe poder saber si un Option tiene valor con isDefined") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    assert(nombre.isDefined)
  }


  test("implementar getOrELse como fold") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val res = nombre.fold{
      "NONAME"
    }{
      x=> x
      }


    assert(res == "NONAME")
  }





  test("Se debe poder acceder al valor de un Option de forma segura con getOrElse") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val res = nombre.getOrElse("NONAME")
    assert(res == "NONAME")
  }

  test("Un Option se debe poder transformar con un map") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    val nombreCompleto: Option[String] = nombre.map(s => s + " Felipe")
    assert(nombreCompleto.getOrElse("NONAME") == "Andres Felipe")
  }

  test("Un Option se debe poder transformar con flatMap en otro Option") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)

    val resultado: Option[String] = nombre.flatMap(s => Option(s.toUpperCase))
    resultado.map( s => assert( s == "ANDRES"))
  }

  test("Un Option se debe poder filtrar con una hof con filter") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_>10)
    val res1 = option1.filter(_>10)

    assert(res0 == None)
    assert(res1 == None)
  }


  test("Un Option se debe poder filtrar con una hof con filter que cumpla para el 5") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_<10) // deuvuelve Some con el valor
    val res1 = option1.filter(_>10)

    assert(res0 == Some(5))
    assert(res1 == None)
  }

  test("for comprehensions en Option") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)

    val resultado = for { //caso feliz porque es Some
      x <- s1 // flecha es una extractor del lado derecho expresiones para extraer un valor
      y <- s2
    } yield x+y // solo vive dentro del for x y y no existe por fuera

    assert(resultado == Some(45))
  }


  test("for comprehensions en Option Con otro some") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)
    val s3 = lista(3)

    val resultado = for { //caso feliz porque es Some
      x <- s1 // flecha es una extractor del lado derecho expresiones para extraer un valor
      y <- s2
      z <- s3
    } yield x+y+z // solo vive dentro del for x y y no existe por fuera

    assert(resultado == Some(65))
  }


  test("for comprehensions en Option  usando funciones foo y bar") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)
    val s3 = lista(3)

    def foo (x:Int): Some[Int]={

      println(s"ejecutando foo ${x}")
      Some(x)
    }

    def bar (x:Int):Option[Int]= {

      println(s"ejecutando bar con ${x}")
      None

    }

    val resultado = for { //caso feliz porque es Some
      x <- foo(1) // flecha es una extractor del lado derecho expresiones para extraer un valor
      y <- bar(2)
      z <- foo(4)
      b <- foo(5)
      c <- foo(6)
      d <- foo(9)
      e <- foo(3)


    } yield x+y+z // solo vive dentro del for x y y no existe por fuera

    assert(resultado == None)
  }


  test("for comprehensions en Option  usando funciones foo y bar con 3 Some y flatMap") {

    val op1 =Some(1)
    val op2 =Some(2)
    val op3 =Some(3)

    val resultado=op1.flatMap{
       x =>
         op2.flatMap( y =>
           op3.flatMap( z=>
             Option(x+y+z)))
    }



    assert(resultado == Some(6))
  }

  test("for comprehesions None en Option") {
    val consultarNombre = Some("Andres")
    val consultarApellido = Some("Estrada")
    val consultarEdad = None
    val consultarSexo = Some("M")

    val resultado = for {
      nom <- consultarNombre
      ape <- consultarApellido
      eda <- consultarEdad
      sex <- consultarSexo
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }

  test("for comprehesions None en Option 2") {// mejor forma  de lo hecho en compresion con flatmap

    def consultarNombre(dni:String): Option[String] = Some("Felix") // composicion monadica
    def consultarApellido(dni:String): Option[String] = Some("Vergara")
    def consultarEdad(dni:String): Option[String] = None
    def consultarSexo(dni:String): Option[String] = Some("M")

    val dni = "8027133"
    val resultado = for {
      nom <- consultarNombre(dni)
      ape <- consultarApellido(dni)
      eda <- consultarEdad(dni)
      sex <- consultarSexo(dni)
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }



  //Alternativas a pattern match sobre options Mejores alternativas


  test("Caso 1 Option pattern match") {


    val option: Option[Int] = Some(5)

    def foo(x: Int) :Option[Int] = {

      Some(x)
      }

    //Opcion poco optima
    val result :Option[Int]= option match {
      case Some(x) => foo(x)
      case None => None
    }

    //Forma correcta
    val result2 :Option[Int] =option.flatMap(foo(_))

    assert(result2 == Some(5))
    assert(result == Some(5))

   }

  test("Caso 2 cuando pattern match es igual qie hacer flatten") {

   val numeros:Option[Option[Int]] = Some(Some(5))


    val res1 : Option[Int]  =numeros match {
      case None => None
      case Some(x) => x
    }

    val res2 : Option[Int]=numeros.flatten

    assert(res1 == Some(5))
    assert(res2 == Some(5))

  }


  test("Caso 3 cuando parent match es igual que hacer map") {


    val option: Option[Int] = Some(4)

    def foo(x: Int) :Option[Int] = {

      Some(x)
    }
// con pattern match
    val result: Option[Option[Int]] =option match {
      case None => None
      case Some(x) => Some(foo(x))

    }
    //funcion con map
    val result2: Option[Option[Int]]=option.map(foo(_))


   // arroja los mismos resultados
    assert(result2==Some(Some(4)))
    assert(result==Some(Some(4)))
  }

  test("Caso 4 cuando pattern match es igual que hacer foreach") {
   val res = Some(1,List(2))
   var i =0
    res.foreach(x=> i+=1)
    assert(i==1)
  }


  test("Caso 5 cuando pattern match es igual que hacer isDefined") {
    val option: Option[Int] = Some(8)

    val result= option match {
      case None => false
      case Some(_) => true
    }

    val result2 = option.isDefined

    assert(result==result2)


  }



  test("Caso 6 cuando pattern match es igual que hacer isEmpty") {
    val option: Option[String] = Some("Damaris")

    val result: Boolean =option match {
      case None => true
      case Some(_) => false
    }
    val result2: Boolean =option.isEmpty

     assert(result==result2)
  }


  test("Caso 7 cuando pattern match es igual que hacer forall") {
    val option  = Some(1,2)

    assert(option.forall( x=> x== (1,2))==true)
  }

  test("Caso 8 cuando pattern match es igual que hacer exists") {
   val option = Some(1)
    val result: Boolean = option.exists(x => x==2)
    assert(result==false)

  }





  test("Caso 11 cuando pattern match es igual que hacer getOrElse") {


  }

  test("Caso 12 cuando pattern match es igual que hacer toList") {


  }

  test("Caso 13 cuando pattern match es igual que hacer coflatMap") {


  }







}

