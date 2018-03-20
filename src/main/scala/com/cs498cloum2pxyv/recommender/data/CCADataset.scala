package com.cs498cloum2pxyv.recommender.data

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import com.cs498cloum2pxyv.recommender.data.weather.{Config, Ingestion}

object CCADataset {

  def main(args: Array[String]): Unit = {
    Ingestion.data(ApplicationExecutionEnvironment.env).first(5).print()
    Ingestion.fields.foreach(f => print(f+"\t"))
  }
}
