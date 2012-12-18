package br.com.ibnetwork.xingu.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.search.SearchEngine;

public class LuceneSearchEngineTest
    extends XinguTestCase
{
    @Inject
    private SearchEngine index;
    
    private static int docId;

    private int indexDoc(String fieldName, String value)
    {
        Document doc = new Document();
        doc.add(new Field(fieldName,value, Store.YES, Index.ANALYZED));
        Long id = new Long(++docId);
        doc.add(new Field("id",id.toString(), Store.YES, Index.ANALYZED));
        index.index(doc);
        return id.intValue();

    }
    
    @Test
    public void testSimpleSearch()
        throws Exception
    {
    	String term = RandomStringUtils.randomAlphabetic(8);
        Query query = index.newQuery(term);
        List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        //index doc
        indexDoc("text","this is some " + term + " text");
        
        //search
        query = index.newQuery(term);
        hits = index.search(query);
        assertEquals(1,hits.size());

        query = index.newQuery("text: " + term);
        hits = index.search(query);
        assertEquals(1,hits.size());
    }

    @Test
    public void testStoreMultipleDocuments()
    	throws Exception
    {
    	String term = RandomStringUtils.randomAlphabetic(8);
    	Query query = index.newQuery(term);
        List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        indexDoc("text","this is some " + term + " text");
        query = index.newQuery(term);
        hits = index.search(query);
        assertEquals(1,hits.size());
        
        indexDoc("text", term + " on other text");
        query = index.newQuery(term);
        hits = index.search(query);
        assertEquals(2,hits.size());
    }
    
    @Test
    public void testDeleteIndex()
    {
    	String term = RandomStringUtils.randomAlphabetic(8);
    	Query query = index.newQuery(term);
    	List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        //add to index
        int id = indexDoc("text", term);
        query = index.newQuery(term);
        hits = index.search(query);
        assertEquals(1,hits.size());
        
        //delete
        Term t1 = new Term("id", String.valueOf(id));
        //Term t2 = new Term("text","_delete_"); //fails
        index.deleteDocs(t1);

        //check
        query = index.newQuery("_delete_");
        hits = index.search(query);
        assertEquals(0,hits.size());
    }
}
