package com.hendraanggrian.kotlinpoet

/**
 * Delimits spec builders' DSL. Code and kdoc builders are not tagged because some specs may
 * implement them.
 */
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class KotlinpoetSpecDsl
