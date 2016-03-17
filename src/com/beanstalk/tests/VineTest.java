package com.beanstalk.tests;

import com.beanstalk.vine.Post;
import com.beanstalk.vine.Vine;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.providers.netty.response.NettyResponse;
import net.jodah.concurrentunit.ConcurrentTestCase;
import net.jodah.concurrentunit.Waiter;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Copyright (c) 2016 Allan Pichardo
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following
 * conditions:
 * <p>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class VineTest extends ConcurrentTestCase{

    @Test
    public void testInitializeSuccess() throws Exception {
        Vine.initialize("allan.pichardo@gmail.com", "NimoIsN01", new Vine.InitializedListener() {
            @Override
            public void onInitialized(Vine vine) {
                threadAssertNotNull(vine);
                threadAssertTrue(vine.isInitialized());
                System.out.println(vine.getSessionId());
                resume();
            }

            @Override
            public void onError(Vine vine, String message) {
                threadAssertNotNull(vine);
                threadAssertTrue(vine.isInitialized());
                resume();
            }
        });
        await();
    }

    @Test
    public void testInitializeError() throws Exception{
        Vine.initialize("", "", new Vine.InitializedListener() {
            @Override
            public void onInitialized(Vine vine) {
                threadAssertNotNull(vine);
                threadAssertTrue(!vine.isInitialized());
                System.out.println(vine.getSessionId());
                resume();
            }

            @Override
            public void onError(Vine vine, String message) {
                threadAssertNotNull(vine);
                threadAssertTrue(!vine.isInitialized());
                resume();
            }
        });
        await();
    }

    @Test
    public void testSearchForTag() throws TimeoutException {
        Vine.initialize("allan.pichardo@gmail.com", "NimoIsN01", new Vine.InitializedListener() {
            @Override
            public void onInitialized(Vine vine) {
                threadAssertNotNull(vine);
                threadAssertTrue(vine.isInitialized());
                vine.searchForTag("cats", new Vine.PostCallbacks() {
                    @Override
                    public void onPostsReturned(List<Post> posts) {
                        threadAssertNotNull(posts);
                        threadAssertTrue(posts.size() > 0);
                        System.out.println(posts.get(0).getVideoUrl());
                        resume();
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println(message);
                        resume();
                    }
                });
            }

            @Override
            public void onError(Vine vine, String message) {
                threadAssertNotNull(vine);
                threadAssertTrue(vine.isInitialized());
                resume();
            }
        });
        await();
    }

}