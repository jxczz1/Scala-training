package co.com.scalatraining.cotizaciones

class Pensionador {

  def limpiarHistoriaLaboral(listaCotizaciones: List[Cotizacion]):Map[String, Int] = {
    listaCotizaciones
      .filter(x => x.diasCotizados != 0 && x.ibc != 0)
      .map(x => Cotizacion(x.periodo, x.aportante, x.diasCotizados, x.ibc * 30 / 15))
      .distinct
      .groupBy(x => (x.periodo, x.aportante))
      .map(x => x._1 -> x._2.foldLeft(0) { (acu, conti) =>
        if (acu < conti.ibc) {
          conti.ibc
        } else {
          acu
        }
      })
      .groupBy(x => x._1._1)
      .map(x => x._1 -> x._2
        .foldLeft(0) { (acu, item) => acu + item._2 })
  }
}