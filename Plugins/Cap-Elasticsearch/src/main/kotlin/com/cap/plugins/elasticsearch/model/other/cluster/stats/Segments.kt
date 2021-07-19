package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Segments(
    val count: Int,
    val memory_in_bytes: Long,
    val terms_memory_in_bytes: Long,
    val stored_fields_memory_in_bytes: Long,
    val term_vectors_memory_in_bytes: Long,
    val norms_memory_in_bytes: Long,
    val doc_values_memory_in_bytes: Long,
    val index_writer_memory_in_bytes: Long,
    val index_writer_max_memory_in_bytes: Long,
    val version_map_memory_in_bytes: Long,
    val fixed_bit_set_memory_in_bytes: Long
)