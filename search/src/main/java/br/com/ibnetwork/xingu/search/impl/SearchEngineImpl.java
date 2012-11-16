package br.com.ibnetwork.xingu.search.impl;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.search.SearchEngine;
import br.com.ibnetwork.xingu.search.SearchEngineException;

public class SearchEngineImpl
	implements SearchEngine
{

	@Override
	public void index(Document doc)
		throws SearchEngineException
	{
		throw new NotImplementedYet();
	}

	@Override
	public List<Document> search(Query query)
		throws SearchEngineException
	{
		throw new NotImplementedYet();
	}

	@Override
	public Query newQuery(String queryString)
		throws SearchEngineException
	{
		throw new NotImplementedYet();
	}

	@Override
	public void deleteDocs(Term term)
		throws SearchEngineException
	{
		throw new NotImplementedYet();
	}

}
