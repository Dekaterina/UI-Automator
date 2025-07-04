package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"
const val TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class ChangeTextTest {

    private lateinit var device: UiDevice
    private val textToSet = "Netology"

    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    @Before
    fun beforeEachTest() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
    }

    @Test
    fun testInternetSettings() {
        waitForPackage(SETTINGS_PACKAGE)
        device.findObject(UiSelector().resourceId("android:id/title").instance(0)).click()
    }

    @Test
    fun testChangeText() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)
        device.findObject(By.res(packageName, "userInput")).text = textToSet
        device.findObject(By.res(packageName, "buttonChange")).click()
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(result, textToSet)
    }

    @Test
    fun testEmptyText() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)
        device.findObject(By.res(packageName, "userInput")).text = ""
        device.findObject(By.res(packageName, "buttonChange")).click()
        val initialText = "Initial Text"
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(initialText, result)
    }

    @Test
    fun testOpenNewActivity() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)
        val newText = "New Text"
        device.findObject(By.res(packageName, "userInput")).text = newText
        device.findObject(By.res(packageName, "buttonStartActivity")).click()
        device.wait(Until.hasObject(By.pkg("$packageName.SecondActivity")), TIMEOUT)
        val secondActivityTextView = device.findObject(By.res(packageName, "secondActivityText"))
        val result = secondActivityTextView.text
        assertEquals(newText, result)
    }
}