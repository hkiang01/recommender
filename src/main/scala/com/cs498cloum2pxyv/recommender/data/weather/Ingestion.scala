package com.cs498cloum2pxyv.recommender.data.weather

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

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
  case class NoaaChicagoDailyTempRaw(
                                   station: String,
                                   name: String,
                                   state: String,
                                   country: String,
                                   date: String,
                                   tmax: String,
                                   tmin: String,
                                   tobs: String)

  def stringToDate(str: String): Date = {
    val sdf = new SimpleDateFormat("MM/dd/yyyy")
    sdf.parse(str)
  }

  def format(raw: NoaaChicagoDailyTempRaw): NoaaChicagoDailyTemp = {

    NoaaChicagoDailyTemp(
      raw.station,
      raw.name,
      raw.state,
      raw.country,
      stringToDate(raw.date),
      raw.tmax,
      raw.tmin,
      raw.tobs
    )
  }

  case class NoaaChicagoDailyTemp(
                                   station: String,
                                   name: String,
                                   state: String,
                                   country: String,
                                   date: Date,
                                   tmax: String,
                                   tmin: String,
                                   tobs: String)

  def csvFile: File = new File("src/main/resources/noaa_chicago_daily_temp.csv")

  def data(env: ExecutionEnvironment): DataSet[NoaaChicagoDailyTemp] = {
    env.readCsvFile[NoaaChicagoDailyTempRaw](
      csvFile.getAbsolutePath,
      pojoFields = fields)
      .map(format(_))
  }

  def noaaStationsTxtFile: File = new File("src/main/resources/ghcnd-stations.txt")

  def noaaStationData(env: ExecutionEnvironment): DataSet[NoaaStation] = {
    env.readTextFile(noaaStationsTxtFile.getAbsolutePath).map(Config.createNoaaStation(_))
  }

  def main(args: Array[String]): Unit = {
    noaaStationData(ApplicationExecutionEnvironment.env).first(5).print()
  }
}
