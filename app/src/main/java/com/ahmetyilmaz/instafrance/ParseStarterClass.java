package com.ahmetyilmaz.instafrance;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Ypwt2HpHTa9hfPlLgqTphgP5w3TdwMth7ssR1BMN")
                .clientKey("Rk9B5UVoGqzgToRltWVznhAjedMapo4JppDBBI1i")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
