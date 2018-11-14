# RedditTop

Simple application thet shows Top 50 posts from Reddit

# Goal
This project was made to try new techniques and improve skills

# Notice

In order to start the whole thing up, you'll need to replace CLIENT_ID in [RedditApi.java] 
```sh
String CLIENT_ID = "your_client_id";
```
with your actual client ID (which you can get [here])

# Tools
This project was made using:
  - MVVM architecture (+ Repository pattertn)
  - Android Architecture Components (s.e. **ViewModel, DataBinding, LiveData**)
  - Retrofit 2

# TODOs
  - (always TOP) Optimize and make it look SOLID
  - adopt to Clean Architecture
  - include RxJava
  - include Dagger 2
  - include local DataSource (caching and local db)
  - adopt and re-write the whole thing in Kotlin


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)
   
   [RedditApi.java]:https://github.com/paul-freez/example.reddittop/blob/master/app/src/main/java/com/testsite/reddittop/data/source/api/RedditApi.java
   [here]:https://github.com/reddit-archive/reddit/wiki/OAuth2
