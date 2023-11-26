package hu.blueberry.googlesheetsintegrationapp.drive.base

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException

interface PermissionHandler {

    fun askForPermissionFromException(exception: UserRecoverableAuthIOException)
}