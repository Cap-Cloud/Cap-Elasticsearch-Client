package com.cap.plugins.common.component.table

abstract class CapTableItem(
    open val index: Int?,
    open val value: Any?
) {
}

class CapTableArrayItem(
    override val index: Int?,
    override val value: Array<out Any?>
) : CapTableItem(index, value) {
}


class CapTableMapItem(
    override val index: Int?,
    override val value: Map<String, out Any?>
) : CapTableItem(index, value) {
}