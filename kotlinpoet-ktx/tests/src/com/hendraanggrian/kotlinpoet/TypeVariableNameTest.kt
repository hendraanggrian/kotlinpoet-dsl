package com.hendraanggrian.kotlinpoet

import com.squareup.kotlinpoet.INT
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeVariableNameTest {

    @Test fun noBounds() = assertEquals("T", "${"T".typeVarOf()}")

    @Test fun classNameBounds() = assertEquals(
        """
            fun <T : kotlin.Int> go() {
            }
            
            """.trimIndent(),
        "${buildFunSpec("go") { typeVariables.add("T", INT) }}"
    )

    @Test fun classBounds() = assertEquals(
        """
            fun <T : java.lang.Integer> go() {
            }
            
            """.trimIndent(),
        "${buildFunSpec("go") { typeVariables.add("T", java.lang.Integer::class.java) }}"
    )

    @Test fun kclassBounds() = assertEquals(
        """
            fun <T : kotlin.Int> go() {
            }
            
            """.trimIndent(),
        "${buildFunSpec("go") { typeVariables.add("T", java.lang.Integer::class) }}"
    )
}