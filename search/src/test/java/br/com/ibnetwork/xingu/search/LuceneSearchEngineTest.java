package br.com.ibnetwork.xingu.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
        Query query = index.newQuery("_value_");
        List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        //index doc
        indexDoc("text","this is some _value_ text");
        
        //search
        query = index.newQuery("_value_");
        hits = index.search(query);
        assertEquals(1,hits.size());

        query = index.newQuery("text: _value_");
        hits = index.search(query);
        assertEquals(1,hits.size());
    }

    @Test
    public void testStoreMultipleDocuments()
    	throws Exception
    {
        Query query = index.newQuery("_other_value_");
        List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        indexDoc("text","this is some _other_value_ text");
        query = index.newQuery("_other_value_");
        hits = index.search(query);
        assertEquals(1,hits.size());
        
        indexDoc("text","_other_value_ on other text");
        query = index.newQuery("_other_value_");
        hits = index.search(query);
        assertEquals(2,hits.size());
    }
    
    @Test
    public void testDeleteIndex()
    {
    	Query query = index.newQuery("_delete_");
    	List<Document> hits = index.search(query);
        assertEquals(0,hits.size());
        
        //add to index
        int id = indexDoc("text","_delete_");
        query = index.newQuery("_delete_");
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
