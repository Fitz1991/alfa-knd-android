package ru.npc_ksb.alfaknd.domain.commands

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class HidingFilterPanelCommand(private val hidingView: @RawValue HidingView) : Command, Parcelable {
    override fun execute() {
        hidingView.hideView()
    }
}