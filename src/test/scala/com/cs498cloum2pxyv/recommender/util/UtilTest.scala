package com.cs498cloum2pxyv.recommender.util

import java.time.{LocalDate, Month}

import org.scalatest.{FlatSpec, Matchers}

class UtilTest extends FlatSpec with Matchers {
  "A date" should "be formatted from a string" in {
    val str = "7/24/2017"
    val date = Util.stringToDate(str)
    val expected = java.sql.Date.valueOf(LocalDate.of(2017, Month.JULY, 24))
    date should be (expected)

    val str2 = "07/24/2017"
    val date2 = Util.stringToDate(str2)
    date2 should be (expected)
  }

  "Slashes and/or dashes" should "be convertable" in {
    val strSlash = ""
  }
}