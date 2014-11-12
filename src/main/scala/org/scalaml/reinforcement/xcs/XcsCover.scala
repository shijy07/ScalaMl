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
package org.scalaml.reinforcement.xcs

import org.scalaml.trading.Signal
import org.scalaml.reinforcement.qlearning.QLState
import scala.util.Random
import org.scalaml.trading.SOperator
import org.scalaml.ga.Discretization


        /**
 		 * <p>Implementation of basic covering algorithm. This covering is invoked
		 * when no rule has a signal or predicate that match the input data. The process
		 * generate random, or pseudo-random rules.</p>
		 * 
		 * @author Patrick Nicolas
		 * @since March 25, 2014
		 * @note Scala for Machine Learning Chapter 11 Reinforcement learning/Extended learning classifier systems
		 */

object XcsCover {  
		/**		 
		 * <p>Generates new rules from an existing population. The number of rules
		 * to generates is the size of the chromosome representing a trading strategy.</p>
		 * @param sensor (or rule predicate) used to generate a set of new rules
		 * @param list of actions (or XCS rules) used in the coverage prpcoess
		 * @throws IllegalArgumenException if the signal is undefined
		 * @return list of new XCS rules. 
		 */
   def cover(sensor: XcsSensor, actions: List[XcsAction])(implicit discr: Discretization): List[XcsRule] = {
  	  require(sensor != null, "XcsCover.cover Cannot generates new rules from undefined signal or predicate")
  	  
  	  actions.foldLeft(List[XcsRule]()) ((xs, act) => {
  	  	 val signal = Signal(sensor.id, sensor.value, new SOperator(Random.nextInt(Signal.numOperators)))
  	  	 new XcsRule(signal, XcsAction(act, Random)) :: xs
  	  })
   } 
}


// -------------------------  EOF -----------------------------------------
