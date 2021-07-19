package com.cap.plugins.common.util

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import com.intellij.util.ExceptionUtil
import com.intellij.util.ReflectionUtil
import java.io.*
import java.lang.reflect.Field
import java.util.function.Consumer

object CloneUtil {

    /**
     * 通过java序列化方式进行克隆
     *
     * @param entity 实体对象
     * @return 克隆后的实体对象
     */
    fun <E : Any> cloneBySerial(entity: E?): E? {
        if (entity == null) {
            return null
        }
        // 没有实现序列化统一使用json方式序列化
        if (entity !is Serializable) {
            return cloneByJson(entity)
        }
        // 定义一个缓冲输出流对象
        val buffer = ByteArrayOutputStream()
        var input: ObjectInputStream? = null
        try {
            ObjectOutputStream(buffer).use { out ->
                // 将对象输出到缓冲区
                out.writeObject(entity)
                // 重新从缓冲区读取对象
                input = ObjectInputStream(ByteArrayInputStream(buffer.toByteArray()))
                return input!!.readObject() as E
            }
        } catch (e: IOException) {
            ExceptionUtil.rethrow(e)
        } catch (e: ClassNotFoundException) {
            ExceptionUtil.rethrow(e)
        } finally {
            // 关闭流
            try {
                buffer.close()
                if (input != null) {
                    input!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 通过JSON序列化方式进行克隆
     *
     * @param entity        实例对象
     * @param copy          是否复制被忽略的属性
     * @param typeReference 返回类型
     * @return 克隆后的实体对象
     */
    fun <E : Any, T : E?> cloneByJson(
        entity: E,
        typeReference: TypeReference<T>? = null,
        copy: Boolean = false
    ): E? {
        if (entity == null) {
            return null
        }
        val objectMapper = ObjectMapper.jsonMapper
        try {
            // 进行序列化
            val json = objectMapper.writeValueAsString(entity)
            // 进行反序列化
            val result = if (typeReference == null) {
                objectMapper.readValue(json, entity.javaClass)
            } else {
                objectMapper.readValue(json, typeReference)
            }
            // 复制被忽略的属性
            if (copy) {
                copyIgnoreProp(entity!!, result!!)
            }
            return result
        } catch (e: IOException) {
            ExceptionUtil.rethrow(e)
        }
        return null
    }

    /**
     * 复制属性
     *
     * @param oldEntity 就实体
     * @param newEntity 新实例
     */
    private fun copyIgnoreProp(oldEntity: Any, newEntity: Any) {
        // 类型不一样直接返回
        if (oldEntity.javaClass != newEntity.javaClass) {
            return
        }
        // 获取所有字段
        val fieldList = ReflectionUtil.collectFields(oldEntity.javaClass)
        if (fieldList.isEmpty()) {
            return
        }
        fieldList.forEach(Consumer { field: Field ->
            if (field.getAnnotation(
                    JsonIgnore::class.java
                ) != null
            ) {
                // 设置允许访问
                field.isAccessible = true
                // 复制字段
                try {
                    field[newEntity] = field[oldEntity]
                } catch (e: IllegalAccessException) {
                    ExceptionUtil.rethrow(e)
                }
            }
        })
    }
}