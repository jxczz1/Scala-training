package co.com.scalatraining.collections

import co.com.scalatraining.cotizaciones.{Cotizacion, Pensionador}
import org.scalatest.FunSuite

class EjercicioCotizaciones extends FunSuite {


  test ("Limpiar Historial"){
    val c1: Cotizacion = new Cotizacion("2018/07", "S4N", 10, 1000)
    val c2: Cotizacion = new Cotizacion("2018/07", "S4N", 20, 0)
    val c3: Cotizacion = new Cotizacion("2018/08", "S4N", 30, 2000)
    val c4: Cotizacion = new Cotizacion("2018/08", "S4N", 30, 2000)
    val c5: Cotizacion = new Cotizacion("2018/09", "S4N", 15, 1000)
    val c6: Cotizacion = new Cotizacion("2018/09", "S4N", 10, 2000)
    val c7: Cotizacion = new Cotizacion("2018/10", "S4N", 15, 1000)
    val c8: Cotizacion = new Cotizacion("2018/10", "UDEA", 15, 5000)
    val listaCotizaciones = List(c1, c2, c3, c4, c5, c6, c7, c8)

    val pensionador: Pensionador = new Pensionador
    assert(pensionador.limpiarHistoriaLaboral(listaCotizaciones) == Map("2018/08" -> 4000, "2018/07" -> 2000, "2018/10" -> 12000, "2018/09" -> 4000))
  }

}
