package com.cs498cloum2pxyv.recommender.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Failure, Success, Try}

object Util {
  def stringToDate(str: String): Date = {
    val sdf = new SimpleDateFormat("MM/dd/yyyy")
    sdf.parse(str)
  }

  val sdf = new SimpleDateFormat("MMddyyyy HH:mm")
  def stringWithDashedTimeToDate(str: String): Date = {
    sdf.parse(str)
  }

  private def defaultDate: Date = new Date()

  def stringWithTimeToDate(str: String): Date = {
    Try(stringWithDashedTimeToDate(str.replace("/","").replace("-",""))) match {
      case Success(x) => x
      case Failure(ex) => { // this isn't good practice, should be "case Failure(ex)"
        defaultDate
      }
    }
  }
}
