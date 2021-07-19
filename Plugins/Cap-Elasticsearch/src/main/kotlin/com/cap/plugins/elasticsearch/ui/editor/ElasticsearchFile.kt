package com.cap.plugins.elasticsearch.ui.editor

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import java.io.InputStream
import java.io.OutputStream

abstract class ElasticsearchFile : VirtualFile() {
    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) {
    }

    override fun getLength(): Long {
        return 0
    }

    override fun getFileType(): FileType {
        return ElasticsearchFakeFileType
    }

    override fun getPath(): String {
        return name
    }

    override fun isDirectory(): Boolean {
        return false
    }

    override fun getTimeStamp(): Long {
        return 0
    }

    override fun contentsToByteArray(): ByteArray {
        return ByteArray(0)
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun getInputStream(): InputStream {
        TODO("Not yet implemented")
    }

    override fun getParent(): VirtualFile? {
        return null
    }

    override fun getChildren(): Array<VirtualFile> {
        return emptyArray()
    }

    override fun isWritable(): Boolean {
        return false
    }

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream {
        TODO("Not yet implemented")
    }
}