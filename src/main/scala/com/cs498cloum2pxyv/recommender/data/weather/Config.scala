package com.cs498cloum2pxyv.recommender.data.weather

object Config {

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

}
