package com.cap.plugins.common.component

class CollectionPageHelper<E>(private val list: List<E>, private val pageSize: Int) : MutableIterator<Collection<E>> {
    private var currPage = 0

    override fun hasNext(): Boolean {
        return currPage * pageSize < list.size
    }

    override fun next(): Collection<E> {
        currPage++
        return getCurrPageData()
    }

    override fun remove() {
    }

    fun hasPrevious(): Boolean {
        return currPage > 1
    }

    fun previous(): Collection<E> {
        currPage--
        return getCurrPageData()
    }

    fun firstPage(): Collection<E> {
        currPage = 1
        return subList(0, pageSize)
    }

    private fun getCurrPageData(): Collection<E> {
        return subList((currPage - 1) * pageSize, currPage * pageSize)
    }

    private fun subList(startIndex: Int, endIndex: Int): Collection<E> {
        return when {
            startIndex >= endIndex -> {
                emptyList()
            }
            list.size <= startIndex -> {
                emptyList()
            }
            list.size < endIndex -> {
                list.subList(startIndex, list.size)
            }
            else -> {
                list.subList(startIndex, endIndex)
            }
        }
    }
}