package com.cs498cloum2pxyv.recommender.data.weather

import java.io.File

import com.cs498cloum2pxyv.recommender.data.weather.Config._
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

object Ingestion {

  def csvFile: File = {
    new File("src/main/resources/noaa_chicago_daily_temp.csv")
  }

  def data(env: ExecutionEnvironment): DataSet[NoaaChicagoDailyTemp] = {
    env.readCsvFile[NoaaChicagoDailyTemp](
      csvFile.getAbsolutePath,
      pojoFields = Config.noaaChicagoDailyTempFields)
  }

  def main(args: Array[String]): Unit = {
    println(csvFile.getAbsolutePath)
//    val env = ApplicationExecutionEnvironment.env
//    println(s"${noaaChicagoDailyTempData(env).count()} temp observations")
  }
}
