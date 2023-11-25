package hu.blueberry.googlesheetsintegrationapp.drive

import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.api.client.googleapis.extensions.android.gms.auth.*
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread


@Singleton
class DriveManager @Inject constructor(
    @ApplicationContext val context: Context
) {
    companion object{

    }
    object MimeType {
        const val FOLDER = "application/vnd.google-apps.folder"
    }

     private fun getDriveService(): Drive? {
        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                context, listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA)
            )
            credential.selectedAccount = googleAccount.account!!
            return Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(),
                credential
            ).build()
        }
        return null
    }


    fun createFolder(name:String, askPermission: (ex:UserRecoverableAuthIOException)->Unit){
        thread {
            try {
                val service = getDriveService()!!

                val folder_data = File()
                folder_data.name = "Now Created/New Folder"
                folder_data.mimeType = MimeType.FOLDER
                val folder = service.files().create(folder_data).execute()

            }catch (ex: UserRecoverableAuthIOException){
                Log.d("Folder", ex.message ?: "")
                askPermission(ex)

            }catch (ex: Exception){
                Log.d("Folder", ex.message ?: "")
            }

        }
    }

    fun searchFolder(){
        thread {
            try {
                val service = getDriveService()!!

                val files = service.files().list()
                files.q = "mimeType='${MimeType.FOLDER}'"

                val result = files.execute()

                result.files.forEach{
                    Log.d("Folder", it.id ?: "no id")
                }

                result.files





            }catch (ex: UserRecoverableAuthIOException){
                Log.d("Folder", ex.message ?: "")


            }catch (ex: Exception){
                Log.d("Folder", ex.message ?: "")
            }

        }
    }

}