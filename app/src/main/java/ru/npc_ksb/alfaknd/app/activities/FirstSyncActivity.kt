@file:Suppress("DEPRECATION")

package ru.npc_ksb.alfaknd.app.activities

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication.Companion.context
import ru.npc_ksb.alfaknd.app.tasks.InspectionNotificationTask
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.domain.sync.FirstSync
import ru.npc_ksb.alfaknd.domain.sync.interfaces.BasicSyncCompleted


class FirstSyncActivity : AppCompatActivity(), BasicSyncCompleted {

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_sync)
        progressBar = findViewById(R.id.progressBar)
        startProgress()
        val viewModel = ViewModelProviders.of(this).get(MainModel::class.java)

        FirstSync(this, viewModel).synchronize()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startProgress() {
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), PorterDuff.Mode.SRC_ATOP)
        progressBar.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        progressBar.visibility = View.GONE
    }

    override fun onTaskCompleted() {
        stopProgress()
        val viewModel = ViewModelProviders.of(this).get(MainModel::class.java)
        InspectionNotificationTask(this, viewModel.inspectionsRepository.getAll())
        startMainActivity()
    }
}
