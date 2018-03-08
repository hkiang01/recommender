package com.cs498cloum2pxyv.recommender.data.weather

import java.io.File

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._


object Ingestion {

  val noaaChicagoDailyTempFields: Array[String] = Array(
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
      pojoFields = noaaChicagoDailyTempFields)
  }

  def main(args: Array[String]): Unit = {
    println(csvFile.getAbsolutePath)
    val env = ApplicationExecutionEnvironment.env
    println(s"${data(env).count()} temp observations")
  }
}
