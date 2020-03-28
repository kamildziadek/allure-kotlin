package io.qameta.allure.sample.junit4.android

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.qameta.allure.android.AllureAndroidJUnit4
import io.qameta.allure.android.extensions.screenshot
import io.qameta.allure.android.rules.LogcatRule
import io.qameta.allure.android.rules.ScreenshotRule
import io.qameta.allure.kotlin.*
import io.qameta.allure.kotlin.Allure.description
import io.qameta.allure.kotlin.Allure.feature
import io.qameta.allure.kotlin.Allure.issue
import io.qameta.allure.kotlin.Allure.step
import io.qameta.allure.kotlin.Allure.story
import io.qameta.allure.kotlin.Allure.tms
import io.qameta.allure.kotlin.junit4.DisplayName
import io.qameta.allure.kotlin.junit4.Tag
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
@Epic("Samples")
@DisplayName("SampleActivity tests")
@Tag("Instrumentation")
class SampleActivityTest {

    @get:Rule
    val ruleChain: RuleChain = RuleChain.outerRule(LogcatRule())
        .around(ScreenshotRule(mode = ScreenshotRule.Mode.END, screenshotName = "screenshot_finish"))

    @Before
    fun setup() {
        ActivityScenario.launch(SampleActivity::class.java)
    }

    @Test
    @Feature("Successes")
    @Story("Annotations")
    @Issue("JIRA-123")
    @TmsLink("TR-123")
    @Owner("Some owner")
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("should text change after click with annotations")
    @Description("Verification of text changing after interaction with a button")
    fun shouldTextChangeAfterClickWithAnnotations() {
        step("Click main button") {
            Allure.screenshot("initial_state")
            Espresso.onView(withId(R.id.button_some)).perform(click())
        }
        val context = ApplicationProvider.getApplicationContext<Context>()
        step("Verify text") {
            Espresso.onView(withId(R.id.label_some))
                .check(matches(withText(context.getString(R.string.after_click))))
        }
    }

    @Test
    @Lead("Some lead")
    @Owner("Some owner")
    @Severity(value = SeverityLevel.BLOCKER)
    @DisplayName("should text change after click with methods")
    fun shouldTextChangeAfterClickWithMethods() {
        feature("Successes")
        story("Methods")
        issue(name = "JIRA-123")
        tms(name = "TR-123")
        description("Verification of text changing after interaction with a button")

        step("Click main button") {
            Allure.screenshot("initial_state")
            Espresso.onView(withId(R.id.button_some)).perform(click())
        }
        val context = ApplicationProvider.getApplicationContext<Context>()
        step("Verify text") {
            Espresso.onView(withId(R.id.label_some))
                .check(matches(withText(context.getString(R.string.after_click))))
        }
    }

    @Test
    @Feature("Failures")
    @Story("Initial state")
    @Issue("JIRA-456")
    @TmsLink("TR-456")
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("should initial state fail")
    @Description("Verification of initial state that should fail on purpose (just so it is shown as a broken test by Allure)")
    fun shouldInitialStateFail() {
        step("Verify init text state") {
            Espresso.onView(withId(R.id.label_some))
                .check(matches(withText("FOO")))

        }
    }

    @Test
    @Ignore("Ignore example")
    @Description("Empty test that ought to be ignored")
    fun shouldIgnore() {
    }

}