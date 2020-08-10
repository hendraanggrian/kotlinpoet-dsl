package com.hendraanggrian.kotlinpoet

import com.hendraanggrian.kotlinpoet.collections.AnnotationSpecList
import com.hendraanggrian.kotlinpoet.collections.AnnotationSpecListScope
import com.hendraanggrian.kotlinpoet.collections.KdocContainer
import com.hendraanggrian.kotlinpoet.collections.KdocContainerScope
import com.hendraanggrian.kotlinpoet.collections.TypeVariableNameList
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import java.lang.reflect.Type
import javax.lang.model.element.Element
import kotlin.reflect.KClass

/** Builds new [PropertySpec] from [TypeName] supplying its name and modifiers. */
fun propertySpecOf(name: String, type: TypeName, vararg modifiers: KModifier): PropertySpec =
    PropertySpec.builder(name, type, *modifiers).build()

/** Builds new [PropertySpec] from [Type] supplying its name and modifiers. */
fun propertySpecOf(name: String, type: Type, vararg modifiers: KModifier): PropertySpec =
    PropertySpec.builder(name, type, *modifiers).build()

/** Builds new [PropertySpec] from [KClass] supplying its name and modifiers. */
fun propertySpecOf(name: String, type: KClass<*>, vararg modifiers: KModifier): PropertySpec =
    PropertySpec.builder(name, type, *modifiers).build()

/** Builds new [PropertySpec] from [T] supplying its name and modifiers. */
inline fun <reified T> propertySpecOf(name: String, vararg modifiers: KModifier): PropertySpec =
    PropertySpec.builder(name, T::class, *modifiers).build()

/**
 * Builds new [PropertySpec] from [TypeName] supplying its name and modifiers,
 * by populating newly created [PropertySpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildPropertySpec(
    name: String,
    type: TypeName,
    vararg modifiers: KModifier,
    builderAction: PropertySpecBuilder.() -> Unit
): PropertySpec = PropertySpec.builder(name, type, *modifiers).build(builderAction)

/**
 * Builds new [PropertySpec] from [Type] supplying its name and modifiers,
 * by populating newly created [PropertySpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildPropertySpec(
    name: String,
    type: Type,
    vararg modifiers: KModifier,
    builderAction: PropertySpecBuilder.() -> Unit
): PropertySpec = PropertySpec.builder(name, type, *modifiers).build(builderAction)

/**
 * Builds new [PropertySpec] from [KClass] supplying its name and modifiers,
 * by populating newly created [PropertySpecBuilder] using provided [builderAction] and then building it.
 */
inline fun buildPropertySpec(
    name: String,
    type: KClass<*>,
    vararg modifiers: KModifier,
    builderAction: PropertySpecBuilder.() -> Unit
): PropertySpec = PropertySpec.builder(name, type, *modifiers).build(builderAction)

/**
 * Builds new [PropertySpec] from [T] supplying its name and modifiers,
 * by populating newly created [PropertySpecBuilder] using provided [builderAction] and then building it.
 */
inline fun <reified T> buildPropertySpec(
    name: String,
    vararg modifiers: KModifier,
    builderAction: PropertySpecBuilder.() -> Unit
): PropertySpec = PropertySpec.builder(name, T::class, *modifiers).build(builderAction)

/** Modify existing [PropertySpec.Builder] using provided [builderAction] and then building it. */
inline fun PropertySpec.Builder.build(
    builderAction: PropertySpecBuilder.() -> Unit
): PropertySpec = PropertySpecBuilder(this).apply(builderAction).build()

/** Wrapper of [PropertySpec.Builder], providing DSL support as a replacement to Java builder. */
@KotlinpoetDslMarker
class PropertySpecBuilder(private val nativeBuilder: PropertySpec.Builder) {

    /** Modifiers of this property. */
    val modifiers: MutableList<KModifier> get() = nativeBuilder.modifiers

    /** Tags variables of this property. */
    val tags: MutableMap<KClass<*>, *> get() = nativeBuilder.tags

    /** Originating elements of this property. */
    val originatingElements: MutableList<Element> get() = nativeBuilder.originatingElements

    /** True to create a `var` instead of a `val`. */
    var isMutable: Boolean
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.mutable(value)
        }

    /** Kdoc of this property. */
    val kdoc: KdocContainer = object : KdocContainer() {
        override fun append(format: String, vararg args: Any) {
            nativeBuilder.addKdoc(format, *args)
        }

        override fun append(code: CodeBlock) {
            nativeBuilder.addKdoc(code)
        }
    }

    /** Configures kdoc of this property. */
    inline fun kdoc(configuration: KdocContainerScope.() -> Unit): Unit =
        KdocContainerScope(kdoc).configuration()

    /** Annotations of this property. */
    val annotations: AnnotationSpecList = AnnotationSpecList(nativeBuilder.annotations)

    /** Configures annotations of this property. */
    inline fun annotations(configuration: AnnotationSpecListScope.() -> Unit): Unit =
        AnnotationSpecListScope(annotations).configuration()

    /** Add property modifiers. */
    fun addModifiers(vararg modifiers: KModifier) {
        nativeBuilder.addModifiers(*modifiers)
    }

    /** Type variables of this property. */
    val typeVariables: TypeVariableNameList = TypeVariableNameList(nativeBuilder.typeVariables)

    /** Initialize field value like [String.format]. */
    fun initializer(format: String, vararg args: Any) {
        nativeBuilder.initializer(format, *args)
    }

    /** Initialize field value with simple string. */
    var initializer: CodeBlock
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.initializer(value)
        }

    /** Initialize field value with custom initialization [builderAction]. */
    inline fun initializer(builderAction: CodeBlockBuilder.() -> Unit) {
        initializer = buildCodeBlock(builderAction)
    }

    /** Delegate field value like [String.format]. */
    fun delegate(format: String, vararg args: Any) {
        nativeBuilder.delegate(format, *args)
    }

    /** Delegate field value with simple string. */
    var delegate: CodeBlock
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.delegate(value)
        }

    /** Delegate field value with custom initialization [builderAction]. */
    inline fun delegate(builderAction: CodeBlockBuilder.() -> Unit) {
        delegate = buildCodeBlock(builderAction)
    }

    /** Set getter function from [FunSpec]. */
    var getter: FunSpec
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.getter(value)
        }

    /** Set getter function. */
    fun getter() {
        nativeBuilder.getter(emptyGetterFunSpec())
    }

    /** Set getter function with custom initialization [builderAction]. */
    inline fun getter(builderAction: FunSpecBuilder.() -> Unit) {
        getter = buildGetterFunSpec(builderAction)
    }

    /** Set setter function from [FunSpec]. */
    var setter: FunSpec
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.setter(value)
        }

    /** Set setter function. */
    fun setter() {
        nativeBuilder.setter(emptySetterFunSpec())
    }

    /** Set setter function with custom initialization [builderAction]. */
    inline fun setter(builderAction: FunSpecBuilder.() -> Unit) {
        setter = buildSetterFunSpec(builderAction)
    }

    /** Set receiver to type. */
    var receiver: TypeName
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
        set(value) {
            nativeBuilder.receiver(value)
        }

    /** Set receiver to [type]. */
    fun receiver(type: Type) {
        nativeBuilder.receiver(type)
    }

    /** Set receiver to [type]. */
    fun receiver(type: KClass<*>) {
        nativeBuilder.receiver(type)
    }

    /** Set receiver to [T]. */
    inline fun <reified T> receiver(): Unit = receiver(T::class)

    /** Returns native spec. */
    fun build(): PropertySpec = nativeBuilder.build()
}
