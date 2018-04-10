package com.cs498cloum2pxyv.recommender.util

import java.time.{LocalDate, Month}

import org.scalatest.{FlatSpec, Matchers}

class UtilTest extends FlatSpec with Matchers {
  "Dates with slashes and/or dashes" should "be converted to Dates" in {
    val strSlash = "12/31/2016 23:45:41"
    val strDash = "2016-12-31 23:45"
    val dateSlash = Util.divvyTripTimeStringToDate(strSlash)
    val dateDash = Util.divvyTripTimeStringToDate(strDash)

    val expected = java.sql.Date.valueOf(LocalDate.of(2016, Month.DECEMBER, 31))
  }

  "Noaa chicago daily temp dates" should "be converted to Dates" in {
    val str = "2016-04-05"
    val date = Util.noaaChicagoDailyTempDateStringToDate(str)
    val expected = java.sql.Date.valueOf(LocalDate.of(2016, Month.APRIL,5))
    date should be (expected)
  }

  "Dates" should "have their times set to midnight" in {
    val str = "12/31/2016 23:45:41"
    val date = Util.divvyTripTimeStringToDate(str)
    Util.removeTime(date).toString.substring(11,19) should equal("00:00:00")
  }
}