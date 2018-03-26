package com.cs498cloum2pxyv.recommender

import org.apache.flink.api.scala.ExecutionEnvironment

object ApplicationExecutionEnvironment {
  val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
}
