package com.example.testnfc

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.testnfc.ndef.NDEFFile

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    lateinit var appContext: Context

    @Before
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext()
        CommandDecoder(NDEFFile(appContext))
    }
}
