package com.cs498cloum2pxyv.recommender.data.weather

import java.io.File

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.weather.Config.NoaaStation
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._


object Ingestion {

  val fields: Array[String] = Array(
    "station",
    "name",
    "state",
    "country",
    "date",
    "tmax",
    "tmin",
    "tobs"
  )
  case class NoaaChicagoDailyTemp(
                                   station: String,
                                   name: String,
                                   state: String,
                                   country: String,
                                   date: String,
                                   tmax: String,
                                   tmin: String,
                                   tobs: String)

  def csvFile: File = new File("src/main/resources/noaa_chicago_daily_temp.csv")

  def data(env: ExecutionEnvironment): DataSet[NoaaChicagoDailyTemp] = {
    env.readCsvFile[NoaaChicagoDailyTemp](
      csvFile.getAbsolutePath,
      pojoFields = fields)
  }

  def noaaStationsTxtFile: File = new File("src/main/resources/ghcnd-stations.txt")

  def noaaStationData(env: ExecutionEnvironment): DataSet[NoaaStation] = {
    env.readTextFile(noaaStationsTxtFile.getAbsolutePath)
      .map(line => {
        Config.createNoaaStation(
          Config.noaaStationFieldsAndIndices.map(tuple => {
            val end = math.min(tuple._3, line.length)
            line.substring(tuple._2-1, end).replace("\\w+", "")
          }))
      })
  }

  def main(args: Array[String]): Unit = {
    noaaStationData(ApplicationExecutionEnvironment.env).first(5).print()
  }
}
