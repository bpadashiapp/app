/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.tahasystem.music.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import ir.tahasystem.music.app.utils.FontsOverride;


public class MyApplication extends Application  {

    private static Application sApplication;


    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        FontsOverride.setDefaultFont(this, "MONOSPACE", "irfont.ttf");
    }


    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static Typeface getFontAwsome() {
        return Typeface.createFromAsset(getContext().getAssets(),
                "wfont.ttf");
    }

    public static Typeface getFontBold() {
        return Typeface.createFromAsset(getContext().getAssets(),
                "irfontnumbold.ttf");
    }

    public static Typeface getFontLight() {
        return Typeface.createFromAsset(getContext().getAssets(),
                "irfontnumlight.ttf");
    }


}