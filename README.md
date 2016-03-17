# Beanstalk
Java library to access [the Vine API](https://github.com/starlock/vino/wiki/API-Reference)

### Features
Currently you can search for tags, which returns a list of posts.

## Usage
Use the static `Vine.initialize()` method to obtain an instance of Vine asynchronously
```java
Vine.initialize("username@domain.com", "password", new Vine.InitializedListener() {
    @Override
    public void onInitialized(Vine vine) {
        // Do stuff
    }

    @Override
    public void onError(Vine vine, String message) {
        // :(
    }
});
```

When you have your Vine, pick away
```java
vine.searchForTag("cats", new Vine.PostCallbacks() {
    @Override
    public void onPostsReturned(List<Post> posts) {
        // do stuff
    }

    @Override
    public void onError(String message) {
        // :(
    }
});
```
