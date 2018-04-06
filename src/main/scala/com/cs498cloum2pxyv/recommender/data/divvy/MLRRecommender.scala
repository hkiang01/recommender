package com.cs498cloum2pxyv.recommender.data.divvy

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.Vector
import org.apache.flink.ml.MLUtils
import org.apache.flink.ml.regression.MultipleLinearRegression
import org.apache.flink.streaming.api.scala._

object MLRRecommender {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val inputDS: DataSet[LabeledVector] = MLUtils.readLibSVM(env, "ML/input/train_feature_label.data")
    val testingDS: DataSet[Vector] = MLUtils.readLibSVM(env, "ML/input/test_feature_label.data").map(_.vector)
    val mlr = MultipleLinearRegression()
      .setIterations(10)
      .setStepsize(0.5)
      .setConvergenceThreshold(0.001)
    mlr.fit(inputDS)
    val predictedRatings = mlr.predict(testingDS)
    predictedRatings.writeAsText("ML/predicted_output")
    env.execute("Flink Recommendation App")
  }
}
