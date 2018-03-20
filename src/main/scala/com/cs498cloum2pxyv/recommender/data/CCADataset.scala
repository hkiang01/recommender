package com.cs498cloum2pxyv.recommender.data

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.weather.Config.NoaaStation
import com.cs498cloum2pxyv.recommender.data.weather.Ingestion.NoaaChicagoDailyTemp
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, JoinDataSet}

object CCADataset {

  val env: ExecutionEnvironment = ApplicationExecutionEnvironment.env

  def main(args: Array[String]): Unit = {

    val temps: DataSet[NoaaChicagoDailyTemp] = weather.Ingestion.data(env)
    val stations: DataSet[NoaaStation] = weather.Ingestion.noaaStationData(env)

    // three interesting DataSets
    val joinedWeather: JoinDataSet[NoaaChicagoDailyTemp, NoaaStation] = temps.join(stations).where("station").equalTo("id")
    val divvyStations = divvy.Ingestion.stationData(env)
    val trips = divvy.Ingestion.tripData(env)
    trips.first(5).print()
  }
}
