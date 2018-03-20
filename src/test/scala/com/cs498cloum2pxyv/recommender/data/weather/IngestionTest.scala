package com.cs498cloum2pxyv.recommender.data.weather

import java.time.{LocalDate, Month}
import java.util.Date

import org.scalatest.{FlatSpec, Matchers}

class IngestionTest extends FlatSpec with Matchers {
  "A date" should "be formatted from a string" in {
    val str = "7/24/2017"
    val date: Date = Ingestion.stringToDate(str)
    val expected = java.sql.Date.valueOf(LocalDate.of(2017, Month.JULY, 24))
    date should be (expected)
  }
}