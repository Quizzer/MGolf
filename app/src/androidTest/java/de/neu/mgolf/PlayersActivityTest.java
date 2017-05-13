package de.neu.mgolf;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlayersActivityTest {

    @Rule
    public ActivityTestRule<PlayersActivity> mActivityTestRule = new ActivityTestRule<PlayersActivity>(PlayersActivity.class);

    @Test
    public void testPlusButtonOnlyEnabledIfTextIsEntered() {

        // Anfangssituation Knopf disabled
        onView(withId(R.id.btnAdd))
                .check(matches(isDisplayed()))
                .check(matches(not(isEnabled())));

        // Eingabe eines Namens
        onView(withId(R.id.edtName)).perform(click(), replaceText("Antonia"), closeSoftKeyboard());

        // Schlusssituation: Knopf enabled
        onView(withId(R.id.btnAdd)).check(matches(isEnabled()));
    }

    @Test
    public void testAddedNameAppearsInList() {

        // Eingabe eines Namens
        onView(withId(R.id.edtName)).perform(click(), replaceText("Anton"), closeSoftKeyboard());

        // Drücke den addButton
        onView(withId(R.id.btnAdd)).perform(click());


        // Schlusssituation: Zweiter Eintrag der Liste enthält "Anton"
        onData(anything())
                .inAdapterView(withId(R.id.lstNames)).atPosition(0)
                .check(matches(withText(R.string.username_default)));
        // Erster Eintrag sollte beim initialen Start der default Spielername sein
        onData(anything())
                .inAdapterView(withId(R.id.lstNames)).atPosition(1)
                .check(matches(withText("Anton")));

    }
}
