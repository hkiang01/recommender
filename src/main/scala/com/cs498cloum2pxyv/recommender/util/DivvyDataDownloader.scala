package com.cs498cloum2pxyv.recommender.util

import java.io.File
import java.net.URL

import scala.sys.process._

object DivvyDataDownloader {

  val dataUrls = List(
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Stations_Trips_2013.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Stations_Trips_2014_Q1Q2.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Stations_Trips_2014_Q3Q4.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2015-Q1Q2.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2015_Q3Q4.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2016_Q1Q2.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2016_Q3Q4.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2017_Q1Q2.zip"),
    new URL("https://s3.amazonaws.com/divvy-data/tripdata/Divvy_Trips_2017_Q3Q4.zip")
  )

  def downloadFiles(): Unit = {
    dataUrls
      .map(url => {
        (url, new File("src/main/resources/" + url.getPath.split("/").last))
      })
      .filter(!_._2.exists)
      .foreach(urlFile => {
        val url = urlFile._1
        val file = urlFile._2
        file.createNewFile()
        url #> file !!
      })
  }

  def main(args: Array[String]): Unit = {
    downloadFiles()
  }

}
