package com.cap.plugins.common.config

interface CapPersistentStateComponent {
    var configs: MutableMap<String, String>
}

class CapPersistentStateComponentAdapter:CapPersistentStateComponent{
    override var configs: MutableMap<String, String> = mutableMapOf()
//    companion object {
//        fun createCredentialAttributes(subsystem: String, key: String): CredentialAttributes {
//            return CredentialAttributes(generateServiceName(subsystem, key))
//        }
//    }
}