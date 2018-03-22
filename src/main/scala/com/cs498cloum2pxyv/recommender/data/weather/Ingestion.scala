package com.cs498cloum2pxyv.recommender.data.weather

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.weather.Config.NoaaStation
import com.cs498cloum2pxyv.recommender.util.Util
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

import scala.util.{Failure, Success, Try}


object Ingestion {

  private def stringToInt(str: String): Int = {
    if (str.isEmpty) Integer.MIN_VALUE
    else Try(str.toInt) match {
      case Success(x) => x
      case Failure(ex) => Integer.MIN_VALUE
    }
  }

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

  def format(raw: NoaaChicagoDailyTempRaw): NoaaChicagoDailyTemp = {
    NoaaChicagoDailyTemp(
      raw.station,
      raw.name,
      raw.state,
      raw.country,
      Util.noaaChicagoDailyTempDateStringToDate(raw.date),
      stringToInt(raw.tmax),
      stringToInt(raw.tmin),
      stringToInt(raw.tobs)
    )
  }

  case class NoaaChicagoDailyTemp(
                                   station: String,
                                   name: String,
                                   state: String,
                                   country: String,
                                   date: Date,
                                   tmax: Int,
                                   tmin: Int,
                                   tobs: Int)

  def isValid(ncdt: NoaaChicagoDailyTemp): Boolean = {
    ncdt.tmin != Integer.MIN_VALUE &&
    ncdt.tmax != Integer.MIN_VALUE &&
    ncdt.tobs != Integer.MIN_VALUE
  }

  def csvFile: File = new File("src/main/resources/noaa_chicago_daily_temp.csv")

  def data(env: ExecutionEnvironment): DataSet[NoaaChicagoDailyTemp] = {
    env.readCsvFile[NoaaChicagoDailyTempRaw](
      csvFile.getAbsolutePath,
      pojoFields = fields,
      lenient = true,
      ignoreFirstLine = true)
      .map(r => format(r))
      .filter(r => isValid(r))
  }

  def noaaStationsTxtFile: File = new File("src/main/resources/ghcnd-stations.txt")

  def noaaStationData(env: ExecutionEnvironment): DataSet[NoaaStation] = {
    env.readTextFile(noaaStationsTxtFile.getAbsolutePath).map(Config.createNoaaStation(_))
  }

  def main(args: Array[String]): Unit = {
    noaaStationData(ApplicationExecutionEnvironment.env).first(5).print()
  }
}
