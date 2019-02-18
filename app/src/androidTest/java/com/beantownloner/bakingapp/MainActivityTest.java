package com.beantownloner.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import com.google.android.exoplayer2.ui.PlayerView;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_NAME_NUTELLA_PIE = "Nutella Pie";
    private static final String RECIPE_NAME_BROWNIES = "Brownies";
    private static final String RECIPE_NAME_YELLOW_CAKE = "Yellow Cake";
    private static final String RECIPE_NAME_CHEESECAKE = "Cheesecake";

    private static final String SAMPLE_INGREDIENT = "Graham Cracker crumbs";
    private static final String SAMPLE_AMT = "2.0 CUP";
    private static final String SAMPLE_STEP = "Recipe Introduction";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkText_RecipeActivity() {
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(0));
        onView(withText(RECIPE_NAME_NUTELLA_PIE)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(1));
        onView(withText(RECIPE_NAME_BROWNIES)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(2));
        onView(withText(RECIPE_NAME_YELLOW_CAKE)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(scrollToPosition(3));
        onView(withText(RECIPE_NAME_CHEESECAKE)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeRecyclerViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recyclerview_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView((withId(R.id.ingredientsTV))).check(matches(withText("Ingredients for " + RECIPE_NAME_NUTELLA_PIE)));
        onView(withId(R.id.recipeStepsRV)).perform(scrollToPosition(0));
        onView(allOf(instanceOf(TextView.class), withText(SAMPLE_STEP))).check(matches(isDisplayed()));
    }

    @Test
    public void clickIngredientsTextView_OpensIngredientsDetailActivity() {
        onView(withId(R.id.recyclerview_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredientsTV)).perform(click());
        onView(withId(R.id.recipeIngredientsRV)).perform(scrollToPosition(0));
        onView(allOf(instanceOf(TextView.class), withText(SAMPLE_INGREDIENT))).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withText(SAMPLE_AMT))).check(matches(isDisplayed()));
    }

    @Test
    public void clickStepRecyclerViewItem_OpensRecipeStep() {
        onView(withId(R.id.recyclerview_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipeStepsRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(instanceOf(PlayerView.class), withId(R.id.video_view))).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
