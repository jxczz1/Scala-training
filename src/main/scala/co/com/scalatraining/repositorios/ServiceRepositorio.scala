package co.com.scalatraining.repositorios

import scala.concurrent.Future

class ServiceRepositorio {


  object OperationsRepositorio {

    def consultar(nombre: String , lista: List[repositorio]): List[repositorio] = {

      val lista2: List[repositorio] = lista
      lista2.filter(x => x==nombre)

    }


    def detalleRepositorio (listaRepoUsuario:Future[List[repositorio]] ) :Future[List[repositorio]] ={

       val listaRepoUsuario2: Future[List[repositorio]] = listaRepoUsuario
        listaRepoUsuario2


    }


  }


}
