# udacity-nanoand-MovieStage2
This is a project for the Udacity - Android Advanced Nanodegree

# Project Overview
Build on MovieStage1, adding the following functionality:
  * List and play trailers for selected Movie in Detail view. Use YouTube App or in browser
  * View Reviews for selected Movie
  * Allow user to mark a Movie as a favorite
  * Create a database to store favorites and other movie data

**IMPORTANT**: You will need to add the following file to the project:
`res/values/keys.xml`

With the following xml:
```xml
<resources>
    <string name="themoviedb">00000000000000000000000000000000</string>
</resources>
```
This is the api key to access the movie api at https://www.themoviedb.org/
You will need to signup and then use your account to request an api key. For more information about the api visit https://www.themoviedb.org/documentation/api

## ATTRIBUTIONS
This project uses the following API's:

### themoviedb
https://www.themoviedb.org/  
https://developers.themoviedb.org/3/getting-started/introduction


The Movie Database (TMDb) is a community built movie and TV database. 

### Picasso
http://square.github.io/picasso/

A powerful image downloading and caching library for Android
