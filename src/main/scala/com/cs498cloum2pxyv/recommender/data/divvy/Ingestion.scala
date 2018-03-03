package com.cs498cloum2pxyv.recommender.data.divvy

import scala.sys.process._
import better.files._

object Ingestion {

  /**
    * Downloads zip files listed by [[Config.urls]] to [[Config.absoluteZipPaths]]
    * If file exists, skip download
    */
  def downloadFiles(): Unit = {
    Config.urlsAndRelativeZipPaths
      .map(elem => (elem._1, new java.io.File(elem._2)))
      .filter(!_._2.exists())
      .foreach(urlFile => {
        val file = urlFile._2
        file.createNewFile()
        urlFile._1 #> file !!
      })
  }

  /**
    * Unzips files from [[Config.absoluteZipPaths]] to [[Config.absoluteExtractPaths]]
    */
  def unzipFiles(): Unit = {
    Config.absoluteZipAndExtractPaths
      .map((p: (String, String)) => {(File(p._1), File(p._2))})
      .filter((p: (File, File)) => !p._2.exists)
      .foreach((p: (File, File)) => {
      p._1.unzipTo(destination = p._2)
    })
  }

  def main(args: Array[String]): Unit = {
    downloadFiles()
    unzipFiles()
  }
}
