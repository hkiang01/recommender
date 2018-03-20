package com.cs498cloum2pxyv.recommender.data.divvy

import com.cs498cloum2pxyv.recommender.ApplicationExecutionEnvironment
import org.apache.flink.api.scala.DataSet
import org.apache.flink.ml.common.ParameterMap
import org.apache.flink.ml.recommendation.ALS
import org.apache.flink.streaming.api.scala._

object ALSRecommender {
  def main(args: Array[String]): Unit = {
    val env = ApplicationExecutionEnvironment.env
//    val inputDS: DataSet[(Int, Int, Int, Int, Int, Int, Double)] =
//      env.readCsvFile[(Int, Int, Int, Int, Int, Int, Double)]("ALS/input/train_feature_label.sixfeatures.txt")
    val inputDS: DataSet[(Int, Int, Double)] =
      env.readCsvFile[(Int, Int,Double)]("ALS/input/train_feature_label.twofeatures.txt")

    val als = ALS()
      .setIterations(10)
      .setNumFactors(10)
      .setBlocks(100)
      .setTemporaryPath("tmp")

    val parameters = ParameterMap()
      .add(ALS.Lambda, 0.9)
      .add(ALS.Seed, 42L)

    als.fit(inputDS, parameters)
//    val testingDS: DataSet[(Int, Int, Int, Int, Int, Int)] =
//      env.readCsvFile[(Int, Int, Int, Int, Int, Int)]("ALS/input/test_feature.sixfeatures.txt")
    val testingDS: DataSet[(Int, Int)] =
      env.readCsvFile[(Int, Int)]("ALS/input/test_feature.twofeatures.txt")

    val predictedRatings = als.predict(testingDS)
    predictedRatings.writeAsCsv("ALS/predicted_output")
    env.execute("Flink Recommendation App")
  }
}
