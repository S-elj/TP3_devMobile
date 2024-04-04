package selj.umontpellier.fragmentapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import selj.umontpellier.fragmentapp.data.SavedPersonalInformation
import java.net.URL

class FileService : Service() {

    private val dataFetching = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + dataFetching)

    companion object {
        const val ACTION_DATA_FETCH_SUCCESS = "selj.umontpellier.fragmentapp.DATA_FETCH_SUCCESS"
        const val ACTION_DATA_FETCH_ERROR = "selj.umontpellier.fragmentapp.DATA_FETCH_ERROR"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val url = URL(intent?.getStringExtra("url"))
            val text = url.readText()
            sendBroadcast(Intent(ACTION_DATA_FETCH_SUCCESS).apply {
                putExtra("personalData", Json.encodeToString(Json.decodeFromString<SavedPersonalInformation>(text)))
            })
        } catch (e: Exception) {
            sendBroadcast(Intent(ACTION_DATA_FETCH_ERROR))
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        dataFetching.cancel()
    }
}