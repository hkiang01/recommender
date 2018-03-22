package com.cs498cloum2pxyv.recommender.data

import java.util.Date

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.divvy.Config
import com.cs498cloum2pxyv.recommender.data.weather.Ingestion.NoaaChicagoDailyTemp
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, JoinDataSet}
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._

case class DateTempMetrics(date: Date,avgMaxTemp: Double, avgMinTemp: Double, avgObsTemp: Double)

object CCADataset {

  val env: ExecutionEnvironment = ApplicationExecutionEnvironment.env

  def main(args: Array[String]): Unit = {

    val temps: DataSet[NoaaChicagoDailyTemp] = weather.Ingestion.data(env)

    // get avg temps by date
    val tempsByDate: DataSet[DateTempMetrics] = temps.groupBy("date").reduceGroup[DateTempMetrics] {
      (in: Iterator[NoaaChicagoDailyTemp], out: Collector[DateTempMetrics]) => {
        val sums = in.map(r => (r.date, r.tmax, r.tmin, r.tobs, 1)).reduce((a,b) => {
          (a._1, a._2 + b._2, a._3 + b._3, a._4 + b._4, a._5 + b._5)
        })
        val dtm = DateTempMetrics(
          sums._1,
          sums._2.toDouble / sums._5.toDouble,
          sums._3.toDouble / sums._5.toDouble,
          sums._4.toDouble / sums._5.toDouble
        )
        out.collect(dtm)
      }
    }

    // join trips with avg temps by date
    val trips: DataSet[Config.Trip] = divvy.Ingestion.tripData(env)
    val tripsAndTemps = trips.join(tempsByDate).where("starttime").equalTo("date")
    tripsAndTemps.first(5).print()
  }
}
