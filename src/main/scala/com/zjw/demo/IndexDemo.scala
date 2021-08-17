package com.zjw.demo

import com.zjw.utils.JacksonUtil
import org.apache.logging.log4j.scala.Logging
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, TextField}
import org.apache.lucene.document.Field.Store
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths
import java.util
import scala.io.Source

object IndexDemo extends Logging {

  case class Production(id: Int, name: String, price: Double, picture: String, description: String)

  def main(args: Array[String]): Unit = {
    // 1. 数据准备
    val source = Source.fromFile("src/main/resources/production.json")
    val jsons = source.getLines.reduceLeft(_ + _)
    source.close()
    val items = JacksonUtil.fromJson(jsons, classOf[Array[Production]])

    // 2. 创建Document文档对象
    val documents = new util.ArrayList[Document]()
    for (item <- items) {
      val document = new Document();
      document.add(new TextField("id", item.id.toString, Store.YES))
      document.add(new TextField("name", item.name, Store.YES))
      document.add(new TextField("price", item.price.toString, Store.YES))
      document.add(new TextField("picture", item.picture, Store.YES))
      document.add(new TextField("description", item.description, Store.YES))
      documents.add(document)
    }
    val analyzer = new StandardAnalyzer()
    val config = new IndexWriterConfig(analyzer)
    val dir = FSDirectory.open(Paths.get("store"))
    val writer = new IndexWriter(dir, config)

    writer.addDocuments(documents)

    writer.close()
  }
}
