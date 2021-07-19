package com.cap.plugins.common.util

import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.service.capPersistentStateComponent
import com.fasterxml.jackson.core.type.TypeReference
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.BackgroundTaskQueue
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

typealias Listener<T> = (T) -> Unit

class MapStringTypeReference : TypeReference<MutableMap<String, String>>()
class MapMapStringTypeReference : TypeReference<MutableMap<String, MutableMap<String, String>>>()

val LOG = Logger.getInstance("Cap-Intellij-Plugin")

val noneProjectQueue = BackgroundTaskQueue(null, "Cap intellij plugins none project queue")

class CancelableTask(
    title: String,
    project: Project?,
    private val task: (ProgressIndicator) -> Unit,
    private val cancelTask: (() -> Unit)? = null
) : Task.Backgroundable(project, title, true) {
    var cancel = false
    override fun run(indicator: ProgressIndicator) {
        task.invoke(indicator)
    }

    override fun onCancel() {
        cancel = true
        cancelTask?.invoke()
        super.onCancel()
    }
}

fun foreground(task: () -> Unit) {
    ApplicationManager.getApplication().invokeLater(task)
}

fun background(
    project: Project?,
    title: String,
    queue: BackgroundTaskQueue,
    task: (ProgressIndicator) -> Unit
) {
    queue.run(CancelableTask(title, project, task, null))
}

fun background(
    project: Project?,
    title: String,
    queue: BackgroundTaskQueue,
    task: (ProgressIndicator) -> Unit,
    cancelTask: (() -> Unit)? = null
) {
    queue.run(CancelableTask(title, project, task, cancelTask))
}

fun background(
    title: String,
    task: (ProgressIndicator) -> Unit
) {
    noneProjectQueue.run(
        CancelableTask(
            title,
            null,
            task,
            null
        )
    )
}

fun background(
    title: String,
    task: (ProgressIndicator) -> Unit,
    cancelTask: (() -> Unit)? = null
) {
    noneProjectQueue.run(
        CancelableTask(
            title,
            null,
            task,
            cancelTask
        )
    )
}

fun showInfoMessage(message: String) {
    LOG.info(message)
    foreground {
        Messages.showInfoMessage(
            message,
            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE)
        )
    }
}

fun showErrorDialog(message: String, e: Throwable?) {
    LOG.error(message, e)
    foreground {
        Messages.showErrorDialog(
            message,
            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE)
        )
    }
}

fun notify(message: String, notification: NotificationType) {
    when (notification) {
        NotificationType.ERROR -> LOG.error(message)
        NotificationType.INFORMATION -> LOG.info(message)
        NotificationType.WARNING -> LOG.warn(message)
    }
    Notifications.Bus.notify(
        Notification(
            CapCommonConstant.NOTIFICATION_GROUP_ID,
            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
            message,
            notification
        )
    )
}

fun notifyInformation(message: String) {
    LOG.info(message)
    Notifications.Bus.notify(
        Notification(
            CapCommonConstant.NOTIFICATION_GROUP_ID,
            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
            message,
            NotificationType.INFORMATION
        )
    )
}

/**
 * 数据格式化
 */
fun <T> format(s: T): Pair<String, String> {
    val value = when (s) {
        is String -> s
        is ByteArray -> String(s)
        else -> s.toString()
    }.trim()
    return when {
        value.startsWith("{") -> {
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                "JSON" to gson.toJson(gson.fromJson(value, Map::class.java))
            } catch (e: JsonSyntaxException) {
                "TEXT" to value
            }
        }
        value.startsWith("<") -> {
            try {
                val dbf = DocumentBuilderFactory.newInstance()
                val db = dbf.newDocumentBuilder()

                val transformer = TransformerFactory.newInstance().newTransformer()
                transformer.setOutputProperty(OutputKeys.INDENT, "yes")
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
                val result = StreamResult(StringWriter())
                val source = DOMSource(db.parse(ByteArrayInputStream(value.toByteArray())))
                transformer.transform(source, result)
                "XML" to result.writer.toString()
            } catch (e: Exception) {
                "TEXT" to value
            }
        }
        else -> "TEXT" to value
    }
}


/**
 * 打印消息记录
 */
fun <T> printMessage(
    editorName: String,
    editorContent: T,
    notifyContent: String
) {
    foreground {
        val (lang, value) = format(editorContent)

        if (capPersistentStateComponent.configs["printToEditorSelected"]?.toBoolean() == true) {
            // explorer.openEditor(editorName, value, lang)
        }
        if (capPersistentStateComponent.configs["printToFileSelected"]?.toBoolean() == true) {
            Files.write(
                Paths.get(capPersistentStateComponent.configs["printToFile"]),
                value.toByteArray(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
            )
        }
        if (capPersistentStateComponent.configs["printToEventSelected"]?.toBoolean() == true) {
            notifyInformation(notifyContent)
        }
    }
}