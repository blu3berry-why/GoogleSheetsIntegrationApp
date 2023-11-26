package hu.blueberry.googlesheetsintegrationapp.drive

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.api.client.googleapis.extensions.android.gms.auth.*
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.sheets.v4.SheetsScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.blueberry.googlesheetsintegrationapp.drive.base.CloudBase
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread


@Singleton
class DriveManager constructor(
    var context: Context,
) {

    var cloudBase: CloudBase = CloudBase(context)

    var scopes = listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA)
    companion object{

    }
    object MimeType {
        const val FOLDER = "application/vnd.google-apps.folder"
        const val SPREADSHEET = "application/vnd.google-apps.spreadsheet"
        const val PDF = "application/pdf"
    }

    val drive: Drive
        get() = getDriveService()!!

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


    fun createFolder(name:String, askPermission: (ex:UserRecoverableAuthIOException)->Unit): String? {

        var folder = File()
            try {
                val folderData = File().apply {
                    this.name = name
                    this.mimeType = MimeType.FOLDER
                }

                folder = drive.files().create(folderData).execute()
            }catch (ex: UserRecoverableAuthIOException){
                Log.d("Folder", ex.message ?: "")
                askPermission(ex)

            }catch (ex: Exception){
                Log.d("Folder", ex.message ?: "")
            }

        return folder.id


    }


    fun searchFolder(name: String): String {

        var folderId = ""
            try {
                val service = getDriveService()!!

                val files = service.files().list()
                files.q = "mimeType='${MimeType.FOLDER}'"

                val result = files.execute()

                result.files.forEach{
                    Log.d("Folder", it.id ?: "no id")
                }

                val folder = result.files.filter {
                    it.name == name
                }.firstOrNull()

                folderId = folder!!.id


            }catch (ex: UserRecoverableAuthIOException){
                Log.d("Folder", ex.message ?: "")


            }catch (ex: Exception){
                Log.d("Folder", ex.message ?: "")
            }

        return folderId

    }

    fun createSpreadSheetInFolder(folderId:String, sheetName:String): String? {
        val folderData = File().apply {
            this.name = sheetName
            this.mimeType = MimeType.SPREADSHEET
            this.parents = listOf(folderId)
        }

       val file =  drive.files().create(folderData).execute()
        return file.id
    }


    fun createFile(name: String, parents: List<String>, mimeType:String): String? {
        var file = File().apply {
            this.name = name
            this.parents = parents
            this.mimeType = mimeType
        }

        file = drive.files().create(file).execute()
        return file.id
    }

}