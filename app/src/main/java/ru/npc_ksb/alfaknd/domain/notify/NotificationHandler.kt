package ru.npc_ksb.alfaknd.domain.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.*
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.activities.MainActivity
import ru.npc_ksb.alfaknd.utils.localDateTimeInMillis
import java.util.*
import java.util.concurrent.TimeUnit


class NotificationHandler(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val title = inputData.getString(Constants.EXTRA_TITLE)
        val text = inputData.getString(Constants.EXTRA_TEXT)
        val id = inputData.getLong(Constants.EXTRA_ID, 0).toInt()
        sendNotification(title, text, id)
        return Result.success()
    }

    private fun sendNotification(title: String?, text: String?, id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.EXTRA_ID, id)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_app)
                .setAutoCancel(true)

        Objects.requireNonNull(notificationManager).notify(id, notification.build())
    }

    companion object {
        fun scheduleReminder(notifyDateTime: LocalDateTime, title:String, text: String, tag: String) {
            val alertTime = localDateTimeInMillis(notifyDateTime) - Calendar.getInstance().timeInMillis
            val data = Data.Builder()
                    .putString(Constants.EXTRA_TITLE, title)
                    .putString(Constants.EXTRA_TEXT, text)
                    .putInt(Constants.EXTRA_ID, NotificationID.id)
                    .build()
            val notificationWork = OneTimeWorkRequest.Builder(NotificationHandler::class.java)
                    .setInitialDelay(alertTime, TimeUnit.MILLISECONDS).addTag(tag)
                    .setInputData(data).build()

            val instance = WorkManager.getInstance()
            instance.enqueue(notificationWork)
        }

        fun cancelReminder(tag: String) {
            val instance = WorkManager.getInstance()
            instance.cancelAllWorkByTag(tag)
        }
    }

}