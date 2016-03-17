package com.beanstalk.vine;

import org.json.JSONObject;

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
public class Post {

    private JSONObject json;

    public Post(JSONObject json){
        this.json = json;
    }

    public String getUsername(){
        return json.optString("username");
    }

    public String getVideoLowUrl(){
        return json.optString("videoLowURL");
    }

    public boolean isLiked(){
        return json.optBoolean("liked");
    }

    public String getVideoUrl(){
        return json.optString("videoUrl");
    }

    public String getDescription(){
        return json.optString("description");
    }

    public String getAvatarUrl(){
        return json.optString("avatarUrl");
    }

    public long getUserId(){
        return json.optLong("userId");
    }

    public long getPostId(){
        return json.optLong("postId");
    }

    public boolean hasExplicitContent(){
        return json.optInt("explicitContent") != 0;
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
