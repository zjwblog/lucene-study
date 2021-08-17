package com.zjw.demo

import org.apache.logging.log4j.scala.Logging
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.{DirectoryReader, IndexWriter}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, ScoreDoc}
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths

/**
 * Created by zjwblog<co.zjwblog@gmail.com> on 2021/8/17
 */
object QueryDemo extends Logging {
  def main(args: Array[String]): Unit = {
    val analyzer = new StandardAnalyzer()
    val parser = new QueryParser("name", analyzer)
    val query = parser.parse("华为")
    val dir = FSDirectory.open(Paths.get("store"))
    val reader = DirectoryReader.open(dir)
    val searcher = new IndexSearcher(reader)

    val docs = searcher.search(query, 2).scoreDocs
    docs.foreach(doc => {
      val docId: Int = doc.doc
      val document: Document = searcher.doc(docId)
      val name = document.getField("name")
      logger.info(name)
    })
  }
}
