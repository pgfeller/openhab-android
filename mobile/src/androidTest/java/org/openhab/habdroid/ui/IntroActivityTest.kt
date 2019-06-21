package org.openhab.habdroid.ui

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.openhab.habdroid.R
import org.openhab.habdroid.TestWithIntro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.openhab.habdroid.TestUtils.childAtPosition

@LargeTest
@RunWith(AndroidJUnit4::class)
class IntroActivityTest : TestWithIntro() {
    @Test
    fun appShowsIntro() {
        val textView = onView(allOf(
                withId(R.id.title),
                withText("Welcome to openHAB"),
                childAtPosition(allOf<View>(withId(R.id.main), withParent(withId(R.id.view_pager))), 0),
                isDisplayed()))
        textView.check(matches(withText("Welcome to openHAB")))

        val imageView = onView(allOf(
                withId(R.id.image),
                childAtPosition(childAtPosition(withId(R.id.main), 1), 0),
                isDisplayed()))
        imageView.check(matches(isDisplayed()))

        val descLabel = "A vendor and technology agnostic open source automation software for your home"
        val textView2 = onView(allOf(
                withId(R.id.description),
                withText(descLabel),
                childAtPosition(childAtPosition(withId(R.id.main), 2), 0),
                isDisplayed()))
        textView2.check(matches(withText(descLabel)))

        val button = onView(allOf<View>(withId(R.id.skip)))
        button.check(matches(isDisplayed()))

        val imageButton = onView(allOf<View>(withId(R.id.next)))
        imageButton.check(matches(isDisplayed()))

        // skip intro
        val appCompatButton = onView(allOf(
                withId(R.id.skip),
                withText("SKIP"),
                childAtPosition(allOf(
                        withId(R.id.bottomContainer),
                        childAtPosition(withId(R.id.bottom), 1)),
                        1),
                isDisplayed()))
        appCompatButton.perform(click())

        appCompatButton.check(doesNotExist())

        activityTestRule.finishActivity()
        activityTestRule.launchActivity(null)

        setupRegisterIdlingResources()

        // Do we see the sitemap?
        val firstfloor = onView(
                allOf<View>(withId(R.id.widgetlabel), withText("First Floor"), isDisplayed()))
        firstfloor.check(matches(withText("First Floor")))
    }

    @Test
    fun goThroughIntro() {
        // click next
        var appCompatImageButton: ViewInteraction
        for (i in 0..2) {
            appCompatImageButton = onView(allOf(
                    withId(R.id.next),
                    childAtPosition(allOf(
                            withId(R.id.bottomContainer),
                            childAtPosition(withId(R.id.bottom), 1)),
                            3),
                    isDisplayed()))
            appCompatImageButton.perform(click())
        }

        // close intro
        val appCompatButton = onView(allOf(
                withId(R.id.done),
                withText("DONE"),
                childAtPosition(allOf(
                        withId(R.id.bottomContainer),
                        childAtPosition(withId(R.id.bottom), 1)),
                        4),
                isDisplayed()))
        appCompatButton.perform(click())

        appCompatButton.check(doesNotExist())

        activityTestRule.finishActivity()
        activityTestRule.launchActivity(null)

        setupRegisterIdlingResources()

        // Do we see the sitemap?
        val firstfloor = onView(
                allOf<View>(withId(R.id.widgetlabel), withText("First Floor"), isDisplayed()))
        firstfloor.check(matches(withText("First Floor")))
    }
}