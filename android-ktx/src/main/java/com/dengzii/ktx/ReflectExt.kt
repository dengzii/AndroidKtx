package com.dengzii.ktx

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*


@Throws(ReflectiveOperationException::class)
fun Class<*>.setField(fieldName: String, value: Any?) {
    getDeclaredField(fieldName).apply {
        val tmp = isAccessible
        set(fieldName, value)
        isAccessible = tmp
    }
}

@Throws(ReflectiveOperationException::class)
fun Class<*>.getField(fieldName: String, default: Any? = null): Any? {
    getDeclaredField(fieldName).apply {
        val tmpAccessible = isAccessible
        val ret = get(this@getField)
        isAccessible = tmpAccessible
        return ret
    }
}

@Throws(ReflectiveOperationException::class)
fun Class<*>.invokeMethod(obj: Any, methodName: String, vararg args: Any): Any? {
    val argsType = args.map { it::class.java }.toTypedArray()
    var method: Method? = null
    try {
        var clazz: Class<*>? = this
        try {
            method = clazz!!.getMethod(methodName, *argsType)
        } catch (e: NoSuchMethodException) {
            do {
                try {
                    method = clazz!!.getDeclaredMethod(methodName, *argsType)
                } catch (_: Exception) {
                }
                clazz = clazz!!.superclass
            } while (clazz != null)
        }
        if (method != null) {
            method.isAccessible = true
            return method.invoke(obj, *args)
        }
        method = getSimilarMethod(methodName, argsType)
        return method?.invoke(obj, *argsType)
    } catch (e: NoSuchMethodException) {

    }
    return null
}

@Throws(NoSuchMethodException::class)
fun Class<*>.getSimilarMethod(name: String, types: Array<Class<*>>): Method? {
    var type: Class<*>? = this
    var methods: List<Method>
    try {
        val isSimilar =
            fun(method: Method, desiredMethod: String, desiredParamTypes: Array<Class<*>>): Boolean {
                return method.name == desiredMethod && mathArguments(method.parameterTypes, desiredParamTypes)
            }

        methods = type!!.methods.filter {
            isSimilar(it, name, types)
        }.toList()

        val comparator = Comparator<Method> { c1, c2 ->
            val types1 = c1.parameterTypes
            val types2 = c2.parameterTypes
            for (i in types1.indices) {
                if (types1[i] != types2[i]) {
                    return@Comparator if (wrapper(types1[i])!!.isAssignableFrom(wrapper(types2[i])!!)) 1 else -1
                }
            }
            return@Comparator 0
        }
        if (methods.isNotEmpty()) {
            return methods.sortedWith(comparator)[0]
        }
        do {
            methods = type!!.declaredMethods.filter {
                isSimilar(it, name, types)
            }.toList()
            if (methods.isNotEmpty()) {
                return methods.sortedWith(comparator)[0]
            }
            type = type.superclass
        } while (type != null)
    } catch (e: Exception) {

    }
    return null
}

@Throws(ReflectiveOperationException::class)
fun Class<*>.newInstance(vararg args: Any?): Any? {
    val argTypes = args.map {
        if (it == null) {
            Null::class.java
        } else {
            it::class.java
        }
    }
    try {
        return getDeclaredConstructor(*argTypes.toTypedArray()).newInstance(args)
    } catch (e: NoSuchMethodException) {
        val constructors = declaredConstructors.filter {
            mathArguments(argTypes.toTypedArray(), it.parameterTypes)
        }.toMutableList()
        if (constructors.isEmpty()) {
            throw IllegalArgumentException("No matching constructor was found.")
        }
        val comparatorPrimitive = Comparator<Constructor<*>> { c1, c2 ->
            val types1 = c1.parameterTypes
            val types2 = c2.parameterTypes
            var t1 = 0
            var t2 = 0
            for (i in types1.indices) {
                if (types1[i].isPrimitive) t1++
                if (types2[i].isPrimitive) t2++
            }
            return@Comparator t1 - t2
        }
        constructors.sortWith(comparatorPrimitive)
        return constructors[0].newInstance(*args)
    }
}

private fun wrapper(type: Class<*>?): Class<*>? {
    type ?: return null
    var ret: Class<*>? = null
    if (type.isPrimitive) {
        ret = when (type) {
            Boolean::class.javaPrimitiveType -> Boolean::class.java
            Int::class.javaPrimitiveType -> Integer::class.java
            Long::class.javaPrimitiveType -> Long::class.java
            Short::class.javaPrimitiveType -> Short::class.java
            Byte::class.javaPrimitiveType -> Byte::class.java
            Double::class.javaPrimitiveType -> Double::class.java
            Float::class.javaPrimitiveType -> Float::class.java
            Char::class.javaPrimitiveType -> Character::class.java
            Void.TYPE -> Void::class.java
            else -> type
        }
    }
    return ret
}

private class Null

private fun mathArguments(argTypes: Array<Class<*>>, declaredTypes: Array<Class<*>>): Boolean {
    if (argTypes.size != declaredTypes.size) {
        return false
    }
    for (i in argTypes.indices) {
        val argType = wrapper(argTypes[i])
        val declareType = wrapper(declaredTypes[i])
        if (argType == Null::class.java || declareType!!.isAssignableFrom(argType!!)) {
            continue
        }
        return false
    }
    return true
}


@Throws(IllegalAccessException::class, IllegalArgumentException::class)
fun Field.setValue(obj: Any, value: Any?) {
    if (isAccessible) {
        set(obj, value)
        return
    }
    val tmpAccessible = isAccessible
    isAccessible = true
    set(obj, value)
    isAccessible = tmpAccessible
}

@Throws(IllegalAccessException::class, IllegalArgumentException::class)
fun Field.getValue(obj: Any?, default: Any? = null): Any? {
    if (isAccessible) {
        return get(obj) ?: default
    }
    val tmpAccessible = isAccessible
    isAccessible = true
    val ret = get(obj) ?: default
    isAccessible = tmpAccessible
    return ret
}

@Throws(ReflectiveOperationException::class)
inline fun <reified T> T.reflect(block: Class<*>.() -> Unit) {
    block.invoke(T::class.java)
}

@Throws(ReflectiveOperationException::class)
inline fun Class<*>.reflect(block: Class<*>.() -> Unit) {
    block.invoke(this)
}

@Throws(ReflectiveOperationException::class)
inline fun reflect(className: String, classLoader: ClassLoader, block: Class<*>.() -> Unit) {
    Class.forName(className, true, classLoader).reflect(block)
}

@Throws(ReflectiveOperationException::class)
inline fun reflect(className: String, block: Class<*>.() -> Unit) {
    Class.forName(className).reflect(block)
}