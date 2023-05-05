package com.example.helpinghand

import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Assert.assertEquals
import org.junit.Rule


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.helpinghand", appContext.packageName)
    }
}


@RunWith(AndroidJUnit4::class)
class MainActivity2InstrumentedTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity2>()

    @Test
    fun testNavigation() {
        onView(withId(R.id.imgBtnHome)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnChat)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnNewPost)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnMyPosts)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnProfile)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
    }
}

