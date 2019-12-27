package ru.npc_ksb.alfaknd.app.components.infoPanel


data class ValueItem(
    val title: String,
    val value: String
)

data class EnityField(
    val name: String,
    val title: String
)

class InfoPanelItems: List<ValueItem> by listOf() {

    companion object {
        fun newValuesFromList(data: List<List<String>>): List<ValueItem>{
            val panelValues = mutableSetOf<ValueItem>()
            data.map{
                panelValues.add(ValueItem(it[0], it[1]))
            }
            return panelValues.toList()
        }

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun newValuesFromEnity(obj: BaseEnity, fields: List<EnityField>): List<ValueItem>{
            val panelValues = mutableSetOf<ValueItem>()
            val fieldNames = fields.map { f -> f.name }

            obj.javaClass.declaredFields.filter { field ->
                field.name in fieldNames
            }.forEach { field ->
                field.isAccessible = true
                val title = fields.find { it.name == field.name }!!.title
                panelValues.add(ValueItem(title, field.get(obj).toString()))
            }
            return panelValues.toList()
        }
    }
}