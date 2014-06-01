package net.nuttle.lucene;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class App 
{
  private static final Directory dir = new RAMDirectory();
  public static void main( String[] args ) throws Exception
  {
      App a = new App();
      a.index();
      a.search();
      a.test();
  }
  
  public void test() throws IOException{
    ByteArrayInputStream is = new ByteArrayInputStream("abcdefABä".getBytes());
    int val = 0;
    while((val=is.read())!=-1) {
      System.out.println(val);
    }
    is.close();
    ByteArrayInputStream bais = new ByteArrayInputStream("ABCDE".getBytes());
  }

  public void search() throws IOException, ParseException {
    Analyzer an = new StandardAnalyzer(Version.LUCENE_30);
    IndexReader reader = IndexReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(reader);
    QueryParser parser = new QueryParser(Version.LUCENE_30, "id", an);
    Query q = parser.parse("title:this+is+a+harrowing+tale");
    TopDocs docs = searcher.search(q, 10);
    System.out.println(reader.numDocs());
    System.out.println("Matches: " + docs.totalHits);
    searcher.close();
    reader.close();
  }

  public void index() throws IOException {
    Analyzer an = new StandardAnalyzer(Version.LUCENE_30);
    IndexWriter writer = new IndexWriter(dir, an, MaxFieldLength.UNLIMITED);
    Document doc = new Document();
    Field f = new Field("id", "ABC1000", Field.Store.NO, Field.Index.NOT_ANALYZED);
    doc.add(f);
    f = new Field("title", "This is a harrowing tale", Field.Store.NO, Field.Index.ANALYZED);
    doc.add(f);
    writer.addDocument(doc);
    writer.close();
    System.out.println("Done indexing");
  }
}
