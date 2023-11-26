package hu.blueberry.googlesheetsintegrationapp.drive.base

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.thread

@Singleton
class CloudBase @Inject constructor(
    @ApplicationContext val context: Context,
    private var permissionHandler: PermissionHandler,
    private var scopes: List<String>
) {


    val credential: GoogleAccountCredential?
        get() {
            GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
                val credential = GoogleAccountCredential.usingOAuth2(
                    context, scopes
                )
                credential.selectedAccount = googleAccount.account!!

                return credential
            }
            return null
        }

    fun errorHandlingRun(func: () -> Unit) {
        thread {
            try {
                func()
            } catch (exception: UserRecoverableAuthIOException) {
                Log.d("Folder", exception.message ?: "")

                permissionHandler.askForPermissionFromException(exception)
            } catch (ex: Exception) {
                Log.d("Folder", ex.message ?: "")
            }
        }
    }


}