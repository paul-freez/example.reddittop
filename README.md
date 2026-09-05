# RedditTop

Simple application thet shows Top (N) posts from Reddit

# Goal
This project was made to try new techniques and improve skills

# Notice

In order to start the whole thing up, you'll need to provide CLIENT_ID & USER_NAME in via `\secrets.properties` file
with your actual client ID (which you can get [here]) and you Reddit username.

# Tools
This project utilizes:
  - MVVM/MVI + Clean Architecture
  - Android Architecture Components, Jetpack Compose
  - Material Design 3
  - Hilt

# TODOs

  - include local DataSource (caching and/or local db)
  - use ApplicationSessionController for session management
  - clean-up Java
  - add test coverage
  - update look and feel of the UI
  - replace XML colors with Compose
  - provide theming (light/dark mode?)



[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)
   
   [here]:https://github.com/reddit-archive/reddit/wiki/OAuth2
