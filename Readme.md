# stop-info-android

Android app which provides information of public transport stations. Current features are:

- display stations around you on a Google Map
- search for stations by name
- display upcoming departures of a specific station

To obtain this data the app makes use of a [stop-info-backend instance](https://github.com/nickbw2003/stop-info-backend).

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="250">][playstore]

[playstore]: https://play.google.com/store/apps/details?id=de.nickbw2003.stopinfo

## Local build setup

1. Google Maps API key
   - Follow `Step 1. Get an API key` of the Google Maps for Android signup guide: https://developers.google.com/maps/documentation/android-sdk/signup
   - Create a `keys.xml` resource file inside `/your-project-root/app/src/main/res/values` and add the Google Maps API key as a string resource as content:
     ```
     <?xml version="1.0" encoding="utf-8"?>
       <resources>
         <string name="maps_api_key" translatable="false">YourApiKey</string>
       </resources>`
     ```
2. Local backend service instance
   - Clone and setup a local instance by following the readme of [stop-info-backend](https://github.com/nickbw2003/stop-info-backend)
3. URL string resources
   - Create a `urls.xml` resource file inside `/your-project-root/app/src/main/res/values`. The file should contain the following string resources:
     ```
     <?xml version="1.0" encoding="utf-8"?>
     <resources>
         <string name="stop_info_service_base_url" translatable="false">http://localhost:3000/api/v1/</string>
         <string name="stop_info_imprint_url" translatable="false">http://example.com/</string>
         <string name="stop_info_privacy_url" translatable="false">http://example.com/</string>
         <string name="play_store_url" translatable="false">http://example.com/</string>
         <string name="twitter_contact_url" translatable="false">http://example.com/</string>
         <string name="mail_contact_url" translatable="false">mailto:example@example.com</string>
     </resources>
     ```
4. Run it!
   - Run the project by using the `localDebug` build variant, which is supporting cleartext http communication to use your local [stop-info-backend instance](https://github.com/nickbw2003/stop-info-backend)

## License
[MIT](LICENSE)
