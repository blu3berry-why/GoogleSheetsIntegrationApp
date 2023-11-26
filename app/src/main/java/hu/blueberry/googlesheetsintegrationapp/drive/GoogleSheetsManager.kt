package hu.blueberry.googlesheetsintegrationapp.drive

import android.content.Context
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.AddSheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse
import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import hu.blueberry.googlesheetsintegrationapp.drive.base.CloudBase
import hu.blueberry.googlesheetsintegrationapp.drive.base.PermissionHandler
import javax.inject.Singleton


@Singleton
class GoogleSheetsManager constructor(
    var permissionHandler: PermissionHandler,
    var context: Context
) {

    var cloudBase: CloudBase = CloudBase(context)

    var scopes = listOf(SheetsScopes.SPREADSHEETS)



    //TODO What if the user is not signed in?
    private fun getSheetsService(): Sheets? {
        return Sheets.Builder(
            NetHttpTransport(), GsonFactory.getDefaultInstance(),
            cloudBase.getCredentials(scopes)
        ).build()
    }

    private val sheets: Sheets
        get() = getSheetsService()!!

    object InputOption {
        val RAW = "RAW"
        val USER_ENTERED = "USER_ENTERED"
    }


    fun createSheet(name: String): String? {
        var spreadsheet = Spreadsheet()
        spreadsheet.properties = SpreadsheetProperties()
            .apply { title = name }

        cloudBase.errorHandlingRun(permissionHandler) {

            spreadsheet = sheets.spreadsheets().create(spreadsheet).execute()
            spreadsheet.properties
        }

        return spreadsheet.spreadsheetId

    }

    fun readSpreadSheet(
        spreadsheetId: String,
        range: String,
    ): ValueRange? {
        var result: ValueRange? = null

        cloudBase.errorHandlingRun(permissionHandler) {
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


        cloudBase.errorHandlingRun(permissionHandler) {
            result = sheets.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .apply {
                    valueInputOption = InputOption.USER_ENTERED
                }
                .execute()
        }
        return result
    }

    fun createNewTab(spreadsheetId: String, name: String): BatchUpdateSpreadsheetResponse {

        var spreadsheet:BatchUpdateSpreadsheetResponse = BatchUpdateSpreadsheetResponse()
        cloudBase.errorHandlingRun(permissionHandler){
           val request = Request()
               .setAddSheet(
                   AddSheetRequest()
                       .apply {
                           this.properties = SheetProperties()
                                                .apply{ this.title = name }
                       }
               )
            val batch = BatchUpdateSpreadsheetRequest().setRequests(listOf(request))

            spreadsheet = sheets.spreadsheets().batchUpdate(spreadsheetId, batch).execute()

        }

        return spreadsheet

    }


}