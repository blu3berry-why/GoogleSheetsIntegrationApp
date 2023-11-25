//package hu.blueberry.googlesheetsintegrationapp.drive
//
//import android.content.Context
//import android.os.Environment
//import android.util.Log
//import com.google.api.client.auth.oauth2.Credential
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
//import com.google.api.client.http.javanet.NetHttpTransport
//import com.google.api.client.json.JsonFactory
//import com.google.api.client.json.gson.GsonFactory
//import com.google.api.client.util.store.FileDataStoreFactory
//import com.google.api.services.drive.Drive
//import com.google.api.services.drive.DriveScopes
//import hu.blueberry.googlesheetsintegrationapp.BuildConfig
//import java.io.File
//import java.io.IOException
//import java.io.InputStreamReader
//import java.security.GeneralSecurityException
//
//
///* class to demonstrate use of Drive files list API */
//class DriveQuickstart(var context: Context) {
//
//
//    companion object {
//        /**
//         * Application name.
//         */
//        val APPLICATION_NAME = "GoogleSheetsIntegrationApp"
//
//        /**
//         * Global instance of the JSON factory.
//         */
//        val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
//
//        /**
//         * Directory to store authorization tokens for this application.
//         */
//        val TOKENS_DIRECTORY_PATH = "GoogleApi/tokens"
//
//        /**
//         * Global instance of the scopes required by this quickstart.
//         * If modifying these scopes, delete your previously saved tokens/ folder.
//         */
//        val SCOPES = listOf(DriveScopes.DRIVE_METADATA_READONLY)
//        val CREDENTIALS_FILE_PATH = "credentials.json"
//    }
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    @Throws(IOException::class)
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
//        // Load client secrets.
//        val `in` =
//            BuildConfig.CREDENTIALS.byteInputStream()
//        val clientSecrets =
//            GoogleClientSecrets.load(
//                JSON_FACTORY,
//                InputStreamReader(`in`)
//            )
//
//        val tokenFolder = File(
//            Environment.getExternalStorageDirectory().toString() +
//                    File.separator + TOKENS_DIRECTORY_PATH
//        )
//        if (!tokenFolder.exists()) {
//            tokenFolder.mkdirs()
//        }
//
//        // Build flow and trigger user authorization request.
//        val flow =
//            GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
//            )
//                .setDataStoreFactory(
//                    FileDataStoreFactory(
//                        tokenFolder
//                    )
//                )
//                .setAccessType("offline")
//                .build()
//        val receiver = LocalServerReceiver.Builder().setPort(8888).build()
//        //returns an authorized Credential object.
//        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
//    }
//
//    @Throws(IOException::class, GeneralSecurityException::class)
//    fun main() {
//        // Build a new authorized API client service.
//        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
//        val service = Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//            .setApplicationName(APPLICATION_NAME)
//            .build()
//
//        // Print the names and IDs for up to 10 files.
//        val result = service.files().list()
//            .setPageSize(10)
//            .setFields("nextPageToken, files(id, name)")
//            .execute()
//        val files = result.files
//        if (files == null || files.isEmpty()) {
//            Log.d("DRIVE", "No files found.")
//        } else {
//            Log.d("DRIVE", "Files:")
//
//            val sb = StringBuilder()
//            for (file in files) {
//
//                val fn = file.name
//                val id = file.id
//
//                sb.appendLine("$fn ($id)")
//
//            }
//
//            Log.d("DRIVE", "$sb")
//        }
//    }
//
//    private fun getTokenFolder(): File {
//        return File(this.context.getExternalFilesDir("")?.absolutePath + TOKENS_DIRECTORY_PATH)
//    }
//}