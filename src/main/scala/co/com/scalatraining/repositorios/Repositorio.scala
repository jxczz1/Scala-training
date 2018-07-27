package co.com.scalatraining.repositorios

import scala.concurrent.Future


 case class repositorio (usuario : String , nombreRepo : String, lineas: String ,lenguaje :String )
 case class repositorioxlenguaje (lista:List[repositorio])
 case class repositorioCompleto (Lista:Future[List[repositorio]])


