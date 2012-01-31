package br.com.ibnetwork.xingu.search;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;

public interface SearchEngine
{
    void index(Document doc)
        throws SearchEngineException;

    List<Document> search(Query query)
        throws SearchEngineException;

    Query newQuery(String queryString)
        throws SearchEngineException;

	void deleteDocs(Term term)
		throws SearchEngineException;

}
