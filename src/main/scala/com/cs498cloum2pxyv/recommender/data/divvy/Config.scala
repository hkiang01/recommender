package com.cs498cloum2pxyv.recommender.data.divvy

import java.io.File
import java.net.URL

object Config {

  val urls = List(
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

  val relativeZipPaths: Seq[String] = urls.map(url => s"src/main/resources/${url.getPath.split("/").last}")
  val absoluteZipPaths: Seq[String] = relativeZipPaths.map(new File(_).getAbsolutePath)
  val relativeExtractPaths: Seq[String] = relativeZipPaths.map(p => {
    val ap = new File(p).getPath
    ap.substring(0, ap.indexOf("."))
  })
  val absoluteExtractPaths: Seq[String] = relativeExtractPaths.map(new File(_).getAbsolutePath)

  val urlsAndRelativeZipPaths: Seq[(URL, String)] = urls.zip(relativeZipPaths)
  val urlsAndAbsoluteZipPaths: Seq[(URL, String)] = urls.zip(absoluteZipPaths)
  val absoluteZipAndExtractPaths: Seq[(String, String)] = absoluteZipPaths.zip(absoluteExtractPaths)
}
