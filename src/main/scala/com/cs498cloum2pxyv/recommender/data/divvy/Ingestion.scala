package com.cs498cloum2pxyv.recommender.data.divvy

import scala.sys.process._
import better.files._
import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.divvy.Config.{Station, Trip, TripRaw}
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

object Ingestion {

  /**
    * Downloads zip files listed by [[Config.urls]] to [[Config.absoluteZipPaths]]
    * If file exists, skip download
    */
  def downloadFiles(): Unit = {
    Config.urlsAndRelativeZipPaths
      .map(elem => (elem._1, new java.io.File(elem._2)))
      .filter(!_._2.exists())
      .foreach(urlFile => {
        val file = urlFile._2
        file.createNewFile()
        urlFile._1 #> file !!
      })
  }

  /**
    * Unzips files from [[Config.absoluteZipPaths]] to [[Config.absoluteExtractPaths]]
    */
  def unzipFiles(): Unit = {
    Config.absoluteZipAndExtractPaths
      .map((p: (String, String)) => {(File(p._1), File(p._2))})
      .filter((p: (File, File)) => !p._2.exists)
      .foreach((p: (File, File)) => {
      p._1.unzipTo(destination = p._2)
    })
  }

  /**
    * Finds csv files from directories listed in [[Config.absoluteExtractPaths]]
    * @return
    */
  def csvFiles: Seq[File] = {
    downloadFiles()
    unzipFiles()
    Config.absoluteExtractPaths.flatMap(p => {
      val dirs = File(p).list.filter(_.isDirectory).toSeq
      val dirFiles = dirs.flatMap(_.list)
      File(p).list.toSeq ++ dirFiles
    }).filter(f => f.name.endsWith(".csv") || f.name.endsWith(".xlsx"))
  }

  /**
    * Retrieves paths of csv files containing [[Station]] data
    * @return
    */
  def stationCsvFiles: Seq[File] = {
    csvFiles.filter(f => {
      val name = f.name
      name.toLowerCase().contains("station") && !name.contains("trip")
    })
  }

  /**
    * Reads csv files containing information on [[Station]]s
    * @param env
    * @return
    */
  def stationData(env: ExecutionEnvironment): DataSet[Station] = {
    stationCsvFiles
      .map(f => {
        env.readCsvFile[Station](f.pathAsString, pojoFields = Config.stationFields, lenient = true)
      }).reduce((ds1, ds2) => ds1.union(ds2))
  }

  /**
    * Retrieves paths of csv files containing [[Trip]] data
    * @return
    */
  def tripCsvFiles: Seq[File] = {
    csvFiles.filter(f => {
      val name = f.name
      name.toLowerCase().contains("trip") && !name.toLowerCase().contains("station")
    })
  }

  /**
    * Reads csv files containing information on [[Trip]]s
    * @param env
    * @return
    */
  def tripData(env: ExecutionEnvironment): DataSet[Trip] = {
    tripCsvFiles
      .map(f => {
        env.readCsvFile[TripRaw](f.pathAsString, pojoFields = Config.tripFields)
          .map[Trip](Config.format(_))
      })
      .reduce((ds1, ds2) => ds1.union(ds2))
  }

  def main(args: Array[String]): Unit = {
    val env = ApplicationExecutionEnvironment.env
    tripData(env).first(5).print()
  }
}
