/**
 * Copyright 2013, 2014  by Patrick Nicolas - Scala for Machine Learning - All rights reserved
 *
 * The source code in this file is provided by the author for the sole purpose of illustrating the 
 * concepts and algorithms presented in "Scala for Machine Learning" ISBN: 978-1-783355-874-2 Packt Publishing.
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * Version 0.95d
 */
package org.scalaml.app.chap8

import org.scalaml.workflow.data.DataSource
import org.scalaml.trading.Fundamentals
import org.scalaml.supervised.svm._
import org.scalaml.core.{XTSeries, types}
import Fundamentals._
import types.ScalaMl._
import org.scalaml.util.Display
import org.apache.log4j.Logger
import scala.util.{Try, Success, Failure}
import org.scalaml.app.Eval


object SVCEval extends Eval {
   val name: String = "SVCEval"
   final val path = "resources/data/chap8/dividends2.csv"	
   final val C = 1.0
   final val GAMMA = 0.5
   final val EPS = 1e-3
   final val NFOLDS = 2
	
   private val logger = Logger.getLogger(name)
   def run(args: Array[String]): Int = {
	   Display.show("SVCEval: Evaluation of Binary Support Vector Classifier", logger)
	   val extractor = relPriceChange :: 
	                   debtToEquity ::
	                   dividendCoverage ::
	                   cashPerShareToPrice ::
	                   epsTrend ::
	                   shortInterest :: 
	                   dividendTrend :: 
	                   List[Array[String] =>Double]()
	   
	   Try {
	       val xs = DataSource(path, true, false, 1) |> extractor
	       val config = SVMConfig(new CSVCFormulation(C), RbfKernel(GAMMA), SVMExecution(EPS, NFOLDS))
	  	   val features = XTSeries.transpose(xs.take(xs.size-1))
		   val svc = SVM[Double](config, features, xs.last)
		     
		   Display.show(s"SVCEval.run ${svc.toString}", logger)
		   svc.accuracy match {
	  	      case Some(acc) => Display.show("SVCEval.run completed", logger)
	  	      case None => Display.error("SVCEval.run accuracy could not be computed", logger)
	  	   }
	  	} match {
	  		case Success(n) => n
	  		case Failure(e) => Display.error("SVCEval.run Could not validate the training set", logger, e)
	  	}
	}
}


object SVCEvalApp extends App {
	SVCEval.run(Array.empty)
}


// --------------------------  EOF -----------------------------------------------