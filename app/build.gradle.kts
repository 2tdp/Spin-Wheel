import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.spin.wheel.chooser.wheeloffortune"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.spin.wheel.chooser.wheeloffortune"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val formattedDate = DateTimeFormatter.ofPattern("MM.dd.yyyy").format(LocalDateTime.now())
        setProperty("archivesBaseName", "App302_SpinWheel($versionCode)${versionName}_${formattedDate}")
    }

    buildTypes {
        debug {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        release {
            isMinifyEnabled = true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    flavorDimensions("default")
    productFlavors {
        create("develop") {
            applicationIdSuffix = ".dev"
            buildConfigField("Long", "Minimum_Fetch", "5L")

            buildConfigField("Long", "cb_fetch_interval", "15L")
            buildConfigField("Boolean", "switch_bannerCollapse_bannerDefault", "true")
            buildConfigField("Boolean", "is_load_banner_collapse_home", "true")
            buildConfigField("Boolean", "is_load_banner", "true")
            buildConfigField("Boolean", "is_load_native_language", "true")
            buildConfigField("Boolean", "is_load_native_language_setting", "true")
            buildConfigField("Boolean", "is_load_native_intro1", "true")
            buildConfigField("Boolean", "is_load_native_intro2", "true")
            buildConfigField("Boolean", "is_load_native_intro3", "true")
            buildConfigField("Boolean", "is_load_native_home", "true")
            buildConfigField("Boolean", "is_load_inter_guide", "true")
            buildConfigField("Boolean", "is_load_inter_back", "true")
            buildConfigField("Boolean", "is_load_inter_roulette", "true")
        }

        create("production") {
            buildConfigField("Long", "Minimum_Fetch", "5L")

            buildConfigField("Long", "cb_fetch_interval", "15L")
            buildConfigField("Boolean", "switch_bannerCollapse_bannerDefault", "true")
            buildConfigField("Boolean", "is_load_banner_collapse_home", "true")
            buildConfigField("Boolean", "is_load_banner", "true")
            buildConfigField("Boolean", "is_load_native_language", "true")
            buildConfigField("Boolean", "is_load_native_language_setting", "true")
            buildConfigField("Boolean", "is_load_native_intro1", "true")
            buildConfigField("Boolean", "is_load_native_intro2", "true")
            buildConfigField("Boolean", "is_load_native_intro3", "true")
            buildConfigField("Boolean", "is_load_native_home", "true")
            buildConfigField("Boolean", "is_load_inter_guide", "true")
            buildConfigField("Boolean", "is_load_inter_back", "true")
            buildConfigField("Boolean", "is_load_inter_roulette", "true")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // rxjava
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("androidx.fragment:fragment-ktx:1.8.1")
    // retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    // hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    //image view
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //lotte animate
    implementation("com.airbnb.android:lottie:6.1.0")


    // Add the dependencies for the Remote Config
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:app-update:2.1.0")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    // firebase service
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    //shimmer fb
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.facebook.infer.annotation:infer-annotation:0.18.0")
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation ("com.intuit.ssp:ssp-android:1.1.1")

    //ads
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.github.devvtn.vtn_ads_libs:ads:1.4.1")
    implementation("com.google.android.gms:play-services-ads:22.5.0")
    //appsflyer
    implementation("com.appsflyer:af-android-sdk:6.12.2")
    implementation("com.appsflyer:adrevenue:6.9.1")

    // mediation
    implementation("com.google.ads.mediation:vungle:7.1.0.0")
    implementation("com.google.ads.mediation:ironsource:7.5.2.0")

    implementation("com.google.ads.mediation:applovin:12.1.0.0")

    implementation("com.google.ads.mediation:mintegral:16.5.51.0")

    implementation("com.google.ads.mediation:pangle:5.6.0.3.0")

    implementation("com.unity3d.ads:unity-ads:4.9.1")
    implementation("com.google.ads.mediation:unity:4.9.2.0")
}

kapt {
    correctErrorTypes = true
}