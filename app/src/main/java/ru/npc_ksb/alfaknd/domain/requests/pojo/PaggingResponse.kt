package ru.npc_ksb.alfaknd.domain.requests.pojo

data class PaggingResponse<T>(
        val data: List<T>,
        val paginator: Paginator
)