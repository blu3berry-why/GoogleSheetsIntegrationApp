

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}



var play_services_version by extra{ "15.0.1" }
var support_version by extra {"27.1.1"}


android {
    namespace = "hu.blueberry.googlesheetsintegrationapp"
    compileSdk = 34



    defaultConfig {
        applicationId = "hu.blueberry.googlesheetsintegrationapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            buildConfigField(
                "String",
                "CREDENTIALS",
                """"{\"installed\":{\"client_id\":\"683827752364-52nasq9dko56s3s17dkpb2bom4mpjtsf.apps.googleusercontent.com\",\"project_id\":\"noted-style-405912\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\"}}""""
            )
        }

        release {
            buildConfigField(
                "String",
                "CREDENTIALS",
                """"{\"installed\":{\"client_id\":\"683827752364-52nasq9dko56s3s17dkpb2bom4mpjtsf.apps.googleusercontent.com\",\"project_id\":\"noted-style-405912\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\"}}""""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")



    //GoogleDrive
    implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")

    //GoogleSheets
    implementation("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")


    //GoogleDependecies
    implementation("com.google.api-client:google-api-client-android:2.0.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.11.0")
    //implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")



    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-drive:17.0.0")




    //dependency injection
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}

kapt{
    correctErrorTypes = true
}





