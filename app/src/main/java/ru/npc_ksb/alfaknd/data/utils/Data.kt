package ru.npc_ksb.alfaknd.data.utils

import io.reactivex.Completable
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.domain.sync.AuthoritySync


fun cleanAllData(viewModel: MainModel): Completable{
    return AuthoritySync(viewModel).cleanData()
            .andThen(InspectionSync(viewModel).cleanData())
}