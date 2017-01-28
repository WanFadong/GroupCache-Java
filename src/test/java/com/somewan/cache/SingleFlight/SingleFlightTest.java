package com.somewan.cache.SingleFlight;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by wan on 2017/1/28.
 */
public class SingleFlightTest extends TestCase {

    public void testSingleDo() throws ExecutionException, InterruptedException {
        final SingleLoader singleLoader = new CacheLoader();
        String[] keys = {null, "name", "age", "name", "name"};
        String[] values = {null, "wanfadong", "25", "wanfadong", "wanfadong"};
        ExecutorService service = Executors.newFixedThreadPool(keys.length);

        final SingleFlight singleFlight = new SingleFlight();
        List<Future<Object>> futureList = new ArrayList<Future<Object>>();
        for(String key: keys) {
            final String thisKey = key;
            Future<Object> future = service.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return singleFlight.singleDo(singleLoader, thisKey);
                }
            });
            futureList.add(future);
        }

        for(int i = 0; i < futureList.size(); i++) {
            Future<Object> future = futureList.get(i);
            assertEquals((String)(future.get()), values[i]);
        }
    }

}
