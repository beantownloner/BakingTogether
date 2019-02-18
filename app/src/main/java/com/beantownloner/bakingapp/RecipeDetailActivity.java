package com.beantownloner.bakingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.objects.Step;
import com.beantownloner.bakingapp.ui.IngredientDetailFragment;
import com.beantownloner.bakingapp.ui.RecipeDetailFragment;
import com.beantownloner.bakingapp.ui.RecipeStepDetailFragment;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepDetailFragment.StepActionListener, RecipeDetailFragment.StepClickListener, RecipeDetailFragment.IngredientClickListener {

    private List<Step> steps;
    private Recipe recipe;
    private int currentPosition;
    private List<Ingredient> ingredients;
    private static String TAG = RecipeDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

            currentPosition = savedInstanceState.getInt("currentPosition");
        } else {
            currentPosition = -1;
        }

        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();

        if (intent != null) {

            if (intent.getExtras() != null) {

                recipe = getIntent().getParcelableExtra("recipe");
                if (recipe != null) {

                    steps = recipe.getSteps();
                    ingredients = recipe.getIngredients();

                }
            }
        }
        setTitle(getString(R.string.title_activity_recipe_detail, recipe.getName()));
        if (!isTablet()) {
            replaceFragmentOne();

            Log.d(TAG, " this is not a tablet ");
        } else {
            Log.d(TAG, " this is  a tablet ");
            replaceFragmentOne();

            if (currentPosition == -1) {
                replaceFragmentThree();
            } else {
                replaceFragmentTwo();

            }

        }

    }

    public void replaceFragmentOne() {

        Log.d(TAG, " currentPosition " + currentPosition);
        RecipeDetailFragment f = new RecipeDetailFragment();
        f.setCurrentStepPosition(currentPosition);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        f.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.recipe_fragment_holder, f)
                .commit();

    }

    public void replaceFragmentTwo() {

        RecipeDetailFragment f = new RecipeDetailFragment();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        f.setArguments(bundle);

        RecipeStepDetailFragment f2 = new RecipeStepDetailFragment();
        bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        f2.setArguments(bundle);
        f2.setStep(steps.get(currentPosition));
        f2.setTablet(isTablet());
        f2.setRecipeName(recipe.getName());
        fm.beginTransaction()
                .replace(R.id.recipe_step_detail_fragment_content, f2)
                .commit();


    }

    public void replaceFragmentThree() {

        IngredientDetailFragment f = new IngredientDetailFragment();
        f.setIngredients(ingredients);
        f.setRecipeName(recipe.getName());
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.recipe_step_detail_fragment_content, f)
                .commit();


    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);

    }

    private boolean isLandscape() {

        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrev() {

    }

    @Override
    public void onStepClicked(int position) {
        currentPosition = position;

        if (isTablet()) {
            RecipeDetailFragment f = (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment_holder);
            f.changeCurrentItemView(currentPosition);
            replaceFragmentTwo();
        } else {

            Step currentStep;
            currentStep = steps.get(currentPosition);
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) steps);
            intent.putExtra("recipename", recipe.getName());
            intent.putExtra("stepid", currentStep.getStepId());
            startActivity(intent);
        }
    }

    @Override
    public void onIngredientClicked() {
        if (!isTablet()) {
            Intent intent = new Intent(this, IngredientsDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) ingredients);
            intent.putExtra("recipename", recipe.getName());
            startActivity(intent);
        } else {
            currentPosition = -1;
            RecipeDetailFragment f = (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment_holder);
            f.changeCurrentItemView(currentPosition);
            replaceFragmentThree();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("currentPosition", currentPosition);
    }
}