------
Title:
------

Google-Plus-Android-Sample

--------
Summary:
--------

This simple Android project presents how the Google+ API can be integrated to Android applications.

-----------------
Operating System:
-----------------

Google Android 2.1+

--------
Details:
--------

The application provides a set of options to the user.
- Option 1: User Authentication. 
-- OAuth 2.0 dance is used with the Google servers to authenticate the user. A
   webview is shown to the user in order to fill the required credentials (username/password).
- Option 2: Profile Access.
-- The user gets information about his profile.
- Option 3: Profile Search.
-- The user can search Google+ profiles "by name" and see their Activity.
- Option 4: Clear Google+ Credentials
-- The application stores the Access-token, Expiration-date and Refresh-token.
   User credentials and tokens are no longer stored.
   
-------------
Installation:
-------------

Clone the project and import it to your Eclipse. The libraries are already
included.

IMPORTANT: Before running your application fill the CLIENT_ID and CLIENT_SECRET
           in the SharedPreferencesCredentialStore class. You can find them at 
           https://code.google.com/apis/console