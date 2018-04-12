package com.cs498cloum2pxyv.recommender.data

import java.util.Date

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.divvy.Config
import com.cs498cloum2pxyv.recommender.data.weather.Ingestion.NoaaChicagoDailyTemp
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, JoinDataSet}
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._

case class DateTempMetrics(date: Date,avgMaxTemp: Double, avgMinTemp: Double, avgObsTemp: Double)

case class CCADataPoint(
                         trip_id: String,
                         starttime: Date,
                         stoptime: Date,
                         bikeid: String,
                         tripduration: String,
                         from_station_id: String,
                         from_station_name: String,
                         to_station_id: String,
                         to_station_name: String,
                         usertype: String,
                         gender: String,
                         birthyear: String,
                         avgMaxTemp: String,
                         avgMinTemp: String,
                         avgObsTemp: String
                       )

object CCADataset {

  val env: ExecutionEnvironment = ApplicationExecutionEnvironment.env

  def tempsByDate(): DataSet[DateTempMetrics] = {
    val temps: DataSet[NoaaChicagoDailyTemp] = weather.Ingestion.data(env)

    temps.groupBy("date").reduceGroup[DateTempMetrics] {
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
  }

  def tripAndWeather(): DataSet[CCADataPoint] = {
    // get avg temps by date
    val temps: DataSet[DateTempMetrics] = tempsByDate()
    println("temps count: " + temps.count())
    // join trips with avg temps by date

    val trips: DataSet[Config.Trip] = divvy.Ingestion.tripsWithStartTimesAtMidnight(env)
    println("trips count: " + trips.count())

    trips.leftOuterJoin(temps).where("starttime").equalTo("date") {
      (trip, temp) => CCADataPoint(
        trip.trip_id,
        trip.starttime,
        trip.stoptime,
        trip.bikeid,
        trip.tripduration,
        trip.from_station_id,
        trip.from_station_name,
        trip.to_station_id,
        trip.to_station_name,
        trip.usertype,
        trip.gender,
        trip.birthyear,
        if (temp == null) "" else temp.avgMaxTemp.toString,
        if (temp == null) "" else temp.avgMinTemp.toString,
        if (temp == null) "" else temp.avgObsTemp.toString
      )
    }
  }

  def main(args: Array[String]): Unit = {
    val data = tripAndWeather()
    println("count: " + data.count())
    data.writeAsCsv("ML/cca_dataset")
    env.execute("Flink Recommendation App")
  }
}
