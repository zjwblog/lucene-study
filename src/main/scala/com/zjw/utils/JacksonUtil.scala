package com.zjw.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.logging.log4j.scala.Logging

import scala.util.control.NonFatal

/**
 * Created by fchen on 2017/8/9.
 */
object JacksonUtil extends Logging {

  private val _mapper = new ObjectMapper()
  _mapper.registerModule(DefaultScalaModule)

  def toJson[T](obj: T): String = {
    _mapper.writeValueAsString(obj)
  }

  def fromJson[T](json: String, `class`: Class[T]): T = {
    try {
        _mapper.readValue(json, `class`)
    } catch {
      case NonFatal(e) =>
        logger.warn(s"can not convert json: [ $json ] to class [ ${`class`} ].")
        null.asInstanceOf[T]
    }
  }

  def prettyPrint[T](obj: T): String = {
    _mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
  }

}
