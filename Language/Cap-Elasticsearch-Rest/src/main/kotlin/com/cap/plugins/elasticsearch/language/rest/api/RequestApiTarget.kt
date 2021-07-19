package com.cap.plugins.elasticsearch.language.rest.api

enum class RequestApiTarget {
    NOT_NEEDED,
    NOT_LIMIT,

    NODES,
    INDICES,
    INDEX_ALIAS,
    DATA_STREAMS,
    JOBS,
    DATA_FRAME_ANALYTICS_JOBS,
    DATAFEEDS,
    FIELDS,
    REPOSITORIES,
    TEMPLATES,
    THREAD_POOLS,
    TRANSFORMS,
    TASKS,
    COMPONENT_TEMPLATE,
    INDEX_UUID,
    INDEX_TEMPLATE,
    LEGACY_INDEX_TEMPLATES,
    POLICY,
    INGEST_PIPELINE
}