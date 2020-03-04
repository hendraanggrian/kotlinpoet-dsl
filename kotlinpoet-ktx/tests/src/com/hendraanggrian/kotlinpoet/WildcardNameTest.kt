package com.hendraanggrian.kotlinpoet

import com.squareup.kotlinpoet.asTypeName
import kotlin.test.Test
import kotlin.test.assertEquals

class WildcardNameTest {
    private companion object {
        const val EXPECTED_SUBTYPE = "out com.hendraanggrian.kotlinpoet.WildcardTypeNameTest.MyClass"
        const val EXPECTED_SUPERTYPE = "in com.hendraanggrian.kotlinpoet.WildcardTypeNameTest.MyClass"
    }

    @Test fun producer() {
        assertEquals(EXPECTED_SUBTYPE, "${MyClass::class.asTypeName().producerOf()}")
        assertEquals(EXPECTED_SUBTYPE, "${MyClass::class.java.producerOf()}")
        assertEquals(EXPECTED_SUBTYPE, "${MyClass::class.producerOf()}")
        assertEquals(EXPECTED_SUBTYPE, "${wildcardTypeNameProducerOf<MyClass>()}")
    }

    @Test fun consumer() {
        assertEquals(EXPECTED_SUPERTYPE, "${MyClass::class.asTypeName().consumerOf()}")
        assertEquals(EXPECTED_SUPERTYPE, "${MyClass::class.java.consumerOf()}")
        assertEquals(EXPECTED_SUPERTYPE, "${MyClass::class.consumerOf()}")
        assertEquals(EXPECTED_SUPERTYPE, "${wildcardTypeNameConsumerOf<MyClass>()}")
    }

    class MyClass
}