package hu.blueberry.googlesheetsintegrationapp.drive

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.blueberry.googlesheetsintegrationapp.drive.base.CloudBase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSheetsManager @Inject constructor(
    @ApplicationContext val context: Context,
    @Inject var cloudBase: CloudBase,
) {

    //TODO What if the user is not signed in?
    private fun getSheetsService(): Sheets? {
        return Sheets.Builder(
            NetHttpTransport(), GsonFactory.getDefaultInstance(),
            cloudBase.credential
        ).build()
    }

    private val sheets: Sheets
        get() = getSheetsService()!!

    object InputOption {
        val RAW = "RAW"
        val USER_ENTERED = "USER_ENTERED"
    }


    fun createSheet(name: String): String {
        var spreadsheet = Spreadsheet()
        spreadsheet.properties = SpreadsheetProperties()
            .apply { title = name }

        cloudBase.errorHandlingRun {
            spreadsheet = sheets.spreadsheets().create(spreadsheet).execute()
        }

        return spreadsheet.spreadsheetId

    }

    fun readSpreadSheet(
        spreadsheetId: String,
        range: String,
    ): ValueRange? {
        var result: ValueRange? = null

        cloudBase.errorHandlingRun {
            result = sheets.spreadsheets().values().get(spreadsheetId, range).execute()
        }
        return result
    }

    fun writeSpreadSheet(
        spreadsheetId: String,
        range: String,
        values: MutableList<MutableList<Any>>
    ): UpdateValuesResponse? {

        var result: UpdateValuesResponse? = null
        val body = ValueRange()
        body.setValues(values)


        cloudBase.errorHandlingRun {
            result = sheets.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .apply {
                    valueInputOption = InputOption.USER_ENTERED
                }
                .execute()
        }

        return result

    }


}