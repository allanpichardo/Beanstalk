package com.beanstalk.vine;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

import java.util.List;

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
public class Vine {

    public static int RESPONSE_CODE_OK = 200;
    public static String HEADER_SESSION_ID = "vine-session-id";
    public static String URL_AUTHENTICATE = "https://api.vineapp.com/users/authenticate";
    public static String URL_GET_TAG = "https://api.vineapp.com/timelines/tags/";

    private static Vine instance;

    private String sessionId = "";

    private Vine(){

    }

    public static void initialize(String email, String password, InitializedListener listener){
        Vine instance = new Vine();
        AsyncHttpClient client = new AsyncHttpClient();
        client.preparePost(URL_AUTHENTICATE)
                .addFormParam("username", email)
                .addFormParam("password",password)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        if (response.hasResponseBody()) {
                            VineResponse vResponse = new VineResponse(response.getResponseBody());
                            if (vResponse.isSuccess()) {
                                instance.storeCredentials(vResponse);
                                listener.onInitialized(instance);
                            } else {
                                instance.warnError(vResponse);
                                listener.onError(instance, vResponse.getError());
                            }
                        } else {
                            listener.onError(instance, "Connection error");
                        }
                        return response;
                    }
                });
    }

    private void storeCredentials(VineResponse vineResponse){
        User user = new User(vineResponse.getData());
        this.sessionId = user.getKey();
    }

    private void warnError(VineResponse vineResponse){
        System.out.println(vineResponse.getError());
    }

    public String getSessionId(){
        return sessionId;
    }

    public boolean isInitialized(){
        return !sessionId.isEmpty();
    }

    public void searchForTag(String tag, PostCallbacks callbacks){
        AsyncHttpClient client = new AsyncHttpClient();
        client.prepareGet(URL_GET_TAG + tag)
                .addHeader(HEADER_SESSION_ID, sessionId)
                .execute(new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        if(response.hasResponseBody()){
                            VineResponse vineResponse = new VineResponse(response.getResponseBody());
                            if(vineResponse.isSuccess()){
                                callbacks.onPostsReturned(new Data(vineResponse.getData()).getPosts());
                            }else{
                                callbacks.onError(vineResponse.getError());
                            }
                        }else{
                            callbacks.onError("Connection error");
                        }
                        return response;
                    }
                });
    }

    public interface InitializedListener{
        void onInitialized(Vine instance);
        void onError(Vine instance, String message);
    }

    public interface PostCallbacks{
        void onPostsReturned(List<Post> posts);
        void onError(String message);
    }
}