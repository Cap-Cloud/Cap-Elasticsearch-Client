package com.cap.plugins.common.service

import com.cap.plugins.common.config.CapPersistentStateComponent
import com.cap.plugins.common.config.CapPersistentStateComponentAdapter
import com.cap.plugins.common.config.CapPersistentStateComponentI18n
import com.cap.plugins.common.config.CapPersistentStateComponentI18nAdapter

var capPersistentStateComponent: CapPersistentStateComponent = CapPersistentStateComponentAdapter()

var capPersistentStateComponentI18n: CapPersistentStateComponentI18n = CapPersistentStateComponentI18nAdapter()