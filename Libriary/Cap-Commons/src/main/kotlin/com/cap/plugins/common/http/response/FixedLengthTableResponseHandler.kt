package com.cap.plugins.common.http.response

class FixedLengthTableResponseHandler : ConvertingResponseHandler<List<List<String>>>({
    val lines = it.lines().filter { line -> line.isNotBlank() }
    val header = lines[0]
    val columnStarts = ArrayList<Int>()
    for (i in header.indices) {
        if (i == 0 || (header[i - 1] == ' ' && header[i] != ' ')) {
            columnStarts.add(i)
        }
    }
    columnStarts.add(Int.MAX_VALUE)
    val columnRanges = columnStarts.zipWithNext()
    lines.asSequence()
        .map { line ->
            columnRanges.asSequence()
                .map { range -> line.substring(range.first, minOf(range.second, line.length)).trim() }
                .toList()
        }.toList()
})