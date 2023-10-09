package com.grayseal.travelhub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This class is annotated with [@HiltAndroidApp](https://dagger.dev/hilt/), which initializes Hilt
 * for dependency injection. It's the entry point for the application's lifecycle.
 */
@HiltAndroidApp
class TravelHubApplication : Application()