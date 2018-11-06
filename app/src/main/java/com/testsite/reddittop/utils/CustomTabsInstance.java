package com.testsite.reddittop.utils;

import androidx.browser.customtabs.CustomTabsIntent;
import saschpe.android.customtabs.CustomTabsHelper;

/**
 * Created by paulf
 */
public class CustomTabsInstance {

    private static CustomTabsHelper HELPER_INSTANCE;

    private CustomTabsInstance() {
    }

    public static CustomTabsHelper getInstance() {
        if (HELPER_INSTANCE == null) {
            HELPER_INSTANCE = new CustomTabsHelper();
        }

        return HELPER_INSTANCE;
    }

    public static class ChromTabsIntent<T> {
        private final CustomTabsIntent intent;
        private final T content;

        public ChromTabsIntent(CustomTabsIntent intent, T content) {
            this.intent = intent;
            this.content = content;
        }

        public CustomTabsIntent getIntent() {
            return intent;
        }

        public T getContent() {
            return content;
        }
    }
}
