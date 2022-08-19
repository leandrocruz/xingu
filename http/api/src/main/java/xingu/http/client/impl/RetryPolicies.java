package xingu.http.client.impl;

import xingu.http.client.RetryPolicy;

import java.util.Arrays;
import java.util.Collections;

public class RetryPolicies
{
    public static final RetryPolicy dftl()
    {
        return times(7);
    }

    public static final RetryPolicy once()
    {
        return times(1);
    }

    public static final RetryPolicy never()
    {
        return times(0);
    }

    public static final RetryPolicy times(int count)
    {
        return new SimpleRetryPolicy(count, Arrays.asList(16, 35, 56));
    }

    public static final RetryPolicy ignoringCurlErrors()
    {
        return new SimpleRetryPolicy(7, Collections.emptyList());
    }
}
