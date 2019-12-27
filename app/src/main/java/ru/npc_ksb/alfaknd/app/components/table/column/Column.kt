package ru.npc_ksb.alfaknd.app.components.table.column

class Column(
    var title: String?,
    var weightColumn: Float?,
    var field: String?,
    var viewClass: Class<*> = TextView::class.java,
    var baseFilter: Int? = null,
    var initFilter: Int? = null
    ) {
    data class Builder(
        var title: String? = null,
        var weightColumn: Float? = null,
        var field: String? = null,
        var viewClass: Class<*> = TextView::class.java,
        var baseFilter: Int? = null,
        var initFilter: Int? = null
        ) {
        fun title(title: String) = apply { this.title = title }
        fun weightColumn(weightColumn: Float) = apply { this.weightColumn = weightColumn }
        fun field(field: String) = apply { this.field = field }
        fun viewClass(viewClass: Class<*>) = apply { this.viewClass = viewClass }
        fun baseFilter(baseFilter: Int) = apply { this.baseFilter = baseFilter }
        fun initFilter(initFilter: Int) = apply { this.initFilter = initFilter }
        fun build() = Column(title, weightColumn, field, viewClass, baseFilter, initFilter)
    }
}




