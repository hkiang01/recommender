package com.cs498cloum2pxyv.recommender.data.divvy

import java.io.File
import java.net.URL
import java.util.Date

import com.cs498cloum2pxyv.recommender.util.Util

import scala.util.{Failure, Success, Try}

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

  val stationFields: Array[String] = Array(
    "id",
    "name",
    "latitude",
    "longitude",
    "dpcapacity",
    "landmark",
    "online date"
  )
  case class Station(
    id: String,
    name: String,
    latitude: String,
    longitude: String,
    dpcapacity: String,
    landmark: String,
    online_date: String
  )

  val tripFields: Array[String] = Array(
      "trip_id",
      "starttime",
      "stoptime",
      "bikeid",
      "tripduration",
      "from_station_id",
      "from_station_name",
      "to_station_id",
      "to_station_name",
      "usertype",
      "gender",
      "birthyear"
  )
  case class TripRaw(
      trip_id: String,
      starttime: String,
      stoptime: String,
      bikeid: String,
      tripduration: String,
      from_station_id: String,
      from_station_name: String,
      to_station_id: String,
      to_station_name: String,
      usertype: String,
      gender: String,
      birthyear: String
  )

  def format(raw: TripRaw): Trip = {
    Trip(
      raw.trip_id,
      Util.stringWithTimeToDate(raw.starttime),
      Util.stringWithTimeToDate(raw.stoptime),
      raw.bikeid,
      raw.tripduration,
      raw.from_station_id,
      raw.from_station_name,
      raw.to_station_id,
      raw.to_station_name,
      raw.usertype,
      raw.gender,
      raw.birthyear
    )
  }

  case class Trip(
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
                   birthyear: String
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
