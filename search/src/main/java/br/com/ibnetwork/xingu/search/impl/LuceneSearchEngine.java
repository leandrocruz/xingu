package br.com.ibnetwork.xingu.search.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.search.SearchEngine;
import br.com.ibnetwork.xingu.search.SearchEngineException;
import br.com.ibnetwork.xingu.utils.FSUtils;

public class LuceneSearchEngine
    implements SearchEngine, Initializable, Configurable
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Inject
    private Factory factory;
    
    private String indexDir;

    private String defaultField;
    
    private Directory directory;

	private String analyserClass;

	private String directoryType;

	private boolean resetIndex;
    
    private Analyzer analyzer;

	private IndexWriter indexModifier;

	@Override
    public void configure(Configuration conf) 
        throws ConfigurationException
    {
        //default field
    	conf = conf.getChild("index");
    	defaultField = conf.getAttribute("defaultField","text");
    	log.info("Default field: "+defaultField);

    	//reset
    	resetIndex = conf.getAttributeAsBoolean("reset", false);
    	log.info("Reset index: "+resetIndex);
    	
    	//analyser
        analyserClass = conf.getAttribute("analyser", null);
        log.info("Analyser class: "+analyserClass);
    	
    	//index dir
    	conf = conf.getChild("directory");
        indexDir = conf.getAttribute("dir","index");
        indexDir = FSUtils.load(indexDir);
        log.info("Index dir: "+indexDir);

        //directory
        directoryType = conf.getAttribute("type","RAMDirectory");
        log.info("Directory type: "+directoryType);
    }
    
	@Override
    public void initialize() 
        throws Exception
    {
        analyzer = analyserClass != null ? (Analyzer) factory.create(analyserClass) : new StandardAnalyzer(Version.LUCENE_30);
        if("FSDirectory".equals(directoryType))
        {
            directory = FSDirectory.open(new File(indexDir));
            if (IndexWriter.isLocked(directory))
            {
                log.info("Directory is locked. Locking it by brute force!");
                IndexWriter.unlock(directory);
            }
        }
        else
        {
        	directory = new RAMDirectory();
        }
        boolean exists = IndexReader.indexExists(directory);
        boolean createOrReset = resetIndex || !exists;
        indexModifier = new IndexWriter(directory, analyzer, createOrReset, MaxFieldLength.UNLIMITED);
    }

    public void dispose()
    {
        unlockIndex();
    }

    private void unlockIndex()
    {
        try
        {
            if(IndexWriter.isLocked(directory))
            {
                IndexWriter.unlock(directory);
            }
        }
        catch (IOException e)
        {   
            log.error("Error unlocking directory: "+directory);
        }
    }


    @Override
    public void index(Document doc) 
        throws SearchEngineException
    {
        try
        {
        	indexModifier.addDocument(doc);
        	indexModifier.commit();
        }
        catch (IOException e)
        {
            throw new SearchEngineException("Error adding document to index",e);
        }
    }

    @Override
    public List<Document> search(Query query) 
        throws SearchEngineException
    {
        try
        {
            //issue #1843: always get the reader from the writer to avoid 'too many open files' error
            IndexReader reader = indexModifier.getReader();
            try
            {
                IndexSearcher searcher = new IndexSearcher(reader);
                List<Integer> hits = perform(query, searcher);
                List<Document> docs = getDocs(hits, searcher);
                return docs;
            }
            finally
            {
                reader.close();
            }
        }
        catch (IOException e)
        {
            throw new SearchEngineException("Error performing search",e);
        }
    }

    private List<Document> getDocs(List<Integer> docNumbers, IndexSearcher searcher)
        throws CorruptIndexException, IOException
    {
        List<Document> docs = new ArrayList<Document>();
        for (int docNum : docNumbers)
        {
            Document doc = searcher.doc(docNum);
            docs.add(doc);
        }
        return docs;
    }

    private List<Integer> perform(Query query, final IndexSearcher searcher)
        throws IOException
    {
        final List<Integer> hits = new ArrayList<Integer>();
        searcher.search(query, new Collector()
        {
            private int docBase;

            @Override
            public void setScorer(Scorer scorer)
                throws IOException
            {}

            @Override
            public void collect(int doc)
                throws IOException
            {
                hits.add(doc + docBase);
            }

            @Override
            public void setNextReader(IndexReader reader, int docBase)
                throws IOException
            {
                this.docBase = docBase;
            }

            @Override
            public boolean acceptsDocsOutOfOrder()
            {
                return true;
            }
        });
        return hits;
    }

    @Override
    public Query newQuery(String queryString) 
        throws SearchEngineException
    {
        QueryParser parser = new QueryParser(Version.LUCENE_30, defaultField, analyzer);
        try
        {
            return parser.parse(queryString);
        }
        catch (ParseException e)
        {
            throw new SearchEngineException("Error creating query from string: "+queryString,e);
        }
    }
    
    @Override
    public void deleteDocs(Term term)
    {
    	try 
        {
    		indexModifier.deleteDocuments(term);
    		indexModifier.commit();
		} 
        catch (IOException e) 
        {
			  throw new SearchEngineException("Error deleting documents: "+term.toString(),e);
		}
    }

}
