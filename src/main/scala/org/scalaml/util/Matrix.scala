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
package org.scalaml.util

import scala.reflect.ClassTag
import scala.util.Random
import org.scalaml.core.types.ScalaMl._


		/**
		 * <p>Class that contains basic Matrix manipulation</p>
		 *  @author Patrick Nicolas
		 *  @since Feb 23, 2014
		 *  @note Scala for Machine Learning
		 */

class Matrix[@specialized(Double, Int) T: ClassTag](val nRows: Int, val nCols: Int, val data: Array[T])(implicit f: T => Double) {
  
   final def apply(i: Int, j: Int): T = data(i*nCols+j)
   def += (i: Int, j : Int, t: T): Unit = data(i*nCols +j) = t
   
   final def diff(that: Matrix[T], error: (T, T) => Double)(implicit f: T => Double): Double = {
  	  require(nCols == that.nCols && nRows == that.nRows, s"cannot compare Matrices of different sizes ($nCols, $nRows) with {${that.nCols}, ${}that.nRows})")
  	  
  	  data.zip(that.data).foldLeft(0.0)((s, xy) => s + error(xy._1, xy._2))
   }
   
   final def cols(i: Int): Array[T] = { 
  	 val idx = i*nRows
  	 data.slice(idx, idx + nRows) 
   }
   
   def /= (iRow: Int, t: T)(implicit g: Double => T): Unit =  {
  	  val i = iRow*nCols
  	  Range(0, nCols).foreach(k => data(i + k) /= t)
   }
   
   def += (iRow: Int, t: T): Unit = {
  	  val i = iRow*nCols
  	  Range(0, nCols).foreach(k => data(i + k) =t)
   }
   
   def transpose: Matrix[T] = {
  	  val m = Matrix[T](nCols, nRows)
  	  Range(0, nRows).foreach(i =>{
  	  	 val col = i*nCols
  	     Range(0, nCols).foreach(j => m += (j, i, data(col+j)))
  	  })
  	  m
   }
   
   @inline
   final def size = nRows*nCols
   
   def fillRandom(mean: Double = 0.0)(implicit f: Double =>T): Unit = 
  	  Range(0, data.size).foreach(i => data.update(i,  mean + Random.nextDouble))
   

   override def toString: String = {
      var count = 0
      data.foldLeft(new StringBuilder)((b, x) => { 
         count += 1; 
         b.append(x).append(  if( count % nCols == 0) "\n" else ",")
      }).toString
   }
}


object Matrix {
   def apply[T: ClassTag](nRows: Int)(implicit f: T => Double): Matrix[T] = apply(nRows, nRows)
   def apply[T: ClassTag](nRows: Int, nCols: Int, data: Array[T])(implicit f: T => Double): Matrix[T] = new Matrix(nRows, nCols, data)
   def apply[T: ClassTag](nRows: Int, nCols: Int)(implicit f: T => Double): Matrix[T] = new Matrix(nRows, nCols, new Array[T](nCols*nRows))
   def apply[T: ClassTag](xy: Array[Array[T]])(implicit f: T => Double): Matrix[T] = new Matrix(xy.size, xy(0).size, xy.flatten)
}

/*
object MatrixApp extends App {
	val x = Array[Array[Double]](
	  Array[Double](11, 12, 13, 14, 15),
	  Array[Double](21, 22, 23, 24, 25),
      Array[Double](31, 32, 33, 34, 35)
    )
	val m = Matrix[Double](x)
	println(s"${m.nRows}, ${m.nCols},: ${m(3, 1)}")
}
* 
*/



// ------------------------------------  EOF ---------------------------------------------