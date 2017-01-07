/*
 * Copyright (C) 2017 Muhammad Tayyab Akram
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mta.tehreer.demo;

import android.app.Application;
import android.content.res.Resources;

import com.mta.tehreer.graphics.Typeface;
import com.mta.tehreer.graphics.TypefaceManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DemoApplication extends Application {

    private static final String ASSET_TAJ_NASTALEEQ = "TajNastaleeq.ttf";
    private static final String ASSET_NAFEES_NASTALEEQ = "NafeesNastaleeq.ttf";
    private static final String ASSET_NAFEES_WEB = "NafeesWeb.ttf";

    private int[] mTypefaceResIds = {
            R.string.typeface_taj_nastaleeq,
            R.string.typeface_nafees_nastaleeq,
            R.string.typeface_nafees_web
    };
    private String[] mTypefaceNames = {
            "AlQalam Taj Nastaleeq",
            "Nafees Nastaleeq",
            "Nafees Web Naskh"
    };

    @Override
    public void onCreate() {
        super.onCreate();

        System.loadLibrary("tehreer");

        Resources resources = getResources();
        registerTypeface(ASSET_TAJ_NASTALEEQ, resources.getString(R.string.typeface_taj_nastaleeq));
        registerTypeface(ASSET_NAFEES_NASTALEEQ, resources.getString(R.string.typeface_nafees_nastaleeq));
        registerTypeface(ASSET_NAFEES_WEB, resources.getString(R.string.typeface_nafees_web));
    }

    public List<String> getTypefaceNames() {
        return Collections.unmodifiableList(Arrays.asList(mTypefaceNames));
    }

    public String getTypefaceTag(int index) {
        return getResources().getString(mTypefaceResIds[index]);
    }

    private void registerTypeface(String fileName, String tag) {
        // it is better to copy the typeface into sdcard for performance reasons.
        try {
            String path = copyAsset(fileName);
            Typeface typeface = Typeface.finalizable(Typeface.createWithFile(path));
            TypefaceManager.registerTypeface(tag, typeface);
        } catch (IOException e) {
            throw new RuntimeException("Unable to register typeface \"" + fileName + "\"");
        }
    }

    private String copyAsset(String fileName) throws IOException {
        String path = getFilesDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(path);

        if (!file.exists()) {
            InputStream in = null;
            OutputStream out = null;

            try {
                in = getAssets().open(fileName);
                out = new FileOutputStream(path);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } finally {
                closeSilently(in);
                closeSilently(out);
            }
        }

        return path;
    }

    private void closeSilently(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
        }
    }
}
