package com.cs498cloum2pxyv.recommender.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Failure, Success, Try}

object Util {
  def stringToDate(str: String): Date = {
    val sdf = new SimpleDateFormat("MM/dd/yyyy")
    sdf.parse(str)
  }

  val sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm")
  def stringWithDashedTimeToDate(str: String): Date = {
    sdf.parse(str)
  }

  val sdf2 = new SimpleDateFormat("MM/dd/yyyy HH:mm")
  def stringWithSlashedTimeToDate(str: String): Date = {
    sdf2.parse(str)
  }

  private def defaultDate: Date = new Date()

  def stringWithTimeToDate(str: String): Date = {
    Try(stringWithSlashedTimeToDate(str)) match {
      case Success(x) => x
      case _ => { // this isn't good practice, should be "case Failure(ex)"
        Try(stringWithDashedTimeToDate(str)) match {
          case Success(x) => x
          case _ => { // this isn't good practice, should be "case Failure(ex)"
            defaultDate
          }
        }
      }
    }
  }
}
