package com.example.sensebox

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class is the object that has the full lifecycle of your application.
 * In order to use Dependency Injection, we need to inherit the Application class and override the onCreate method.
 * Specify the container class with services in it. But Hilt framework will do everything for us automatically.
 * It's enough just to annotate @HiltAndroidApp
 */

@HiltAndroidApp
class MainApplication : Application()