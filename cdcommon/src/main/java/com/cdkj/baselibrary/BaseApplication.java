
package com.cdkj.baselibrary;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
	/**
	 * Global application context.
	 */
	private static Context sContext;

	/**
	 * Construct of LitePalApplication. Initialize application context.
	 */
	public BaseApplication() {
		sContext = this;
	}

	public static void initialize(Context context) {
        sContext = context;
    }

	public static Context getContext() {
		return sContext;
	}

}
