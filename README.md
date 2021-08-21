# chara

This is the intern project I done with McKesson. It is an Android app with its main purpose being a work-from-home app. 
The concept idea, prototype and design was completed with another intern.
All the icons are custom made using Inkscape.

We are using Googles Firebase Cloud Storage as the Database for the app along with a few other features Firebase offers like the Authnentication and Storage features.
When a user wants to log in, register, reset their password, edit their account details or add a task to their calendar, data is pushed/pulled from the database.

There are 3 main features on it:
The Chatbot - tells jokes, helps you navigate through the app, gives advice.
The Calendar - create tasks in the calendar and view them in the todays list screen or when you click on the date in the calender screen.
The Video Library contains a couple of videos that we thought would be helpful for people who are working from home.

The Chatbot (Chara)
The Chatbot is hosted on a Flask server and is trained with the intents.json file. It communicates with the Android app using Retrofit through API requests.

The Calendar
The Calendar is created using a recyclerview, viewholder and adaptor.

The Video Library
The Video Library is created using an open-source library on Github by PierfrancescoSoffritti. 
You must make sure the API version for your app is 17 or over to use this library.
Here is a link to that repository if you would like to explore more: https://github.com/PierfrancescoSoffritti/android-youtube-player
