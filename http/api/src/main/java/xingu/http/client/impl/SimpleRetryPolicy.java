package xingu.http.client.impl;

import xingu.http.client.RetryPolicy;

import java.util.List;

public class SimpleRetryPolicy implements RetryPolicy {

    private final int count;

    private final List<Integer> results;

    public SimpleRetryPolicy(int count, List<Integer> results)
    {
        this.count = count;
        this.results = results;
    }

    @Override
    public boolean retry(int result)
    {
        return count >= 0 && results.contains(result);
    }

    @Override
    public RetryPolicy update(int result)
    {
        return new SimpleRetryPolicy(count - 1, results);
    }
}
