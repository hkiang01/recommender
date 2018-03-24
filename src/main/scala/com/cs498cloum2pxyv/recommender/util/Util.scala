package com.cs498cloum2pxyv.recommender.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Failure, Success, Try}
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Util {
  private val logger = LogManager.getLogger(this.getClass.getName)

  private def defaultDate: Date = new Date()
  def sdfDivvyDashedDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
  def sdfDivvySlashedDateTimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss")
  def sdfDivvyDashedDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def divvyTripTimeStringToDate(str: String): Date = {
    Try(sdfDivvyDashedDateTimeFormat.parse(str)) match {
      case Success(x) => x
      case Failure(ex) => Try(sdfDivvySlashedDateTimeFormat.parse(str)) match {
        case Success(x2) => x2
        case Failure(ex2) => defaultDate
      }
    }
  }

  val sdfNoaaChicagoDailyTempString = new SimpleDateFormat("yyyy-MM-dd")
  def noaaChicagoDailyTempDateStringToDate(str: String): Date = {
    if(str.isEmpty) {
      return defaultDate
    }
    Try(sdfNoaaChicagoDailyTempString.parse(str)) match {
      case Success(x) => x
      case Failure(ex) => {
//        logger.error(s"unable to convert $str to date", ex)
        defaultDate
      }
    }
  }
}
