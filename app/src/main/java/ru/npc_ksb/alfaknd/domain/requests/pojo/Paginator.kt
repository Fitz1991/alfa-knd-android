package ru.npc_ksb.alfaknd.domain.requests.pojo

data class Paginator(
        val count: Int? = null,
        val page: Int? = null,
        val total_count: Int? = null,
        val num_pages: Int? = null,
        val start_index: Int? = null,
        val end_index: Int? = null
)