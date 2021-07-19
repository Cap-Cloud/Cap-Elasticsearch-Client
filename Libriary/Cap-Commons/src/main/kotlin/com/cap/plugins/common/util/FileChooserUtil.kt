package com.cap.plugins.common.util

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.vfs.VirtualFile

/**
 * FileChooser工具类
 *
 * @author cap_cloud
 * @version 1.0.0
 */
object FileChooserUtil {
    /**
     * 选择路径
     *
     * @return
     */
    fun choosePath(): VirtualFile? {
        val descriptor =
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        return FileChooser.chooseFile(descriptor, null, null)
    }

    /**
     * 选择数据库驱动
     *
     * @return
     */
    fun chooseDriverJars(): Array<VirtualFile> {
        val descriptor =
            FileChooserDescriptor(false, false, true, true, false, true)
                .withTitle("Select Database Driver Files")
        return FileChooser.chooseFiles(descriptor, null, null)
    }
}