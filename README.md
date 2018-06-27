# udacity-nanoand-MovieStage2
This is a project for the Udacity - Android Advanced Nanodegree

# Project Overview
This 'Popular Movie App' presents a scrollable grid of Movie posters (sorted by 'popularity', 'top rated' or 'user favorites'). Tap on a movie poster for a detail page showing:
  * movie poster
  * plot summary 
  * rating
  * popularity
  * release date 
  
In this version (Stage 2) the following functionality is added:
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

### VERY Helpful posts:
  * How to get YouTube preview images: https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
  * Toggle Button for Favories: https://stackoverflow.com/questions/34980309/favourite-button-android
  * Android vector drawables: https://developer.android.com/studio/write/vector-asset-studio
  * AsyncTask and Loader frameworks -good! : https://google-developer-training.gitbooks.io/android-developer-fundamentals-course-concepts/content/en/Unit%203/71c_asynctask_and_asynctaskloader_md.html
  * Android Intents:  https://developer.android.com/training/basics/intents/sending
  * Material guide - helpful : https://material.io/design/layout/spacing-methods.html#baseline
  * Adding a RatingBar :  https://www.mkyong.com/android/android-rating-bar-example/
  * PopupWindow to show full review text: https://developer.android.com/reference/android/widget/PopupWindow
  * Good post about styling radio buttons: https://stackoverflow.com/questions/19163628/adding-custom-radio-buttons-in-android


