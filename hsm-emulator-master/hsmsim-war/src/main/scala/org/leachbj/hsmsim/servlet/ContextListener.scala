/**
 * Copyright (c) 2013 Bernard Leach
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.leachbj.hsmsim.servlet

import _root_.akka.Main
import javax.servlet.{ServletContextListener, ServletContextEvent}
import akka.actor.ActorSystem
import akka.actor.Props
import org.leachbj.hsmsim.akka.HsmSimulator
import org.apache.log4j.Logger
 
 /**
  * This class can be added to web.xml mappings as a listener to start and postStop Akka.
  *<web-app>
  * ...
  *  <listener>
  *    <listener-class>com.my.Initializer</listener-class>
  *  </listener>
  * ...
  *</web-app>
  */
class ContextListener extends ServletContextListener {
  val log = Logger.getLogger(classOf[ContextListener])
  
  lazy val system = ActorSystem("Main")
  lazy val sim = system.actorOf(Props[HsmSimulator])
   def contextDestroyed(e: ServletContextEvent): Unit = system.stop(sim)
   def contextInitialized(e: ServletContextEvent): Unit = {
    log.debug("Actor " + sim + " started")
    system.registerOnTermination(log.debug("Akka exited"))
  }
 }
