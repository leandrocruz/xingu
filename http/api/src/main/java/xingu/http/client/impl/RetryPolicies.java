package xingu.http.client.impl;

import xingu.http.client.RetryPolicy;

import java.util.Arrays;
import java.util.Collections;

public class RetryPolicies
{
    public static RetryPolicy dftl()
    {
        return times(7);
    }

    public static RetryPolicy once()
    {
        return times(1);
    }

    public static RetryPolicy never()
    {
        return times(0);
    }

    public static RetryPolicy times(int count)
    {
        return new SimpleRetryPolicy(count, Arrays.asList(16, 35, 56));
    }

    public static RetryPolicy ignoringCurlErrors()
    {
        return new SimpleRetryPolicy(7, Collections.emptyList());
    }
}
