package com.beantownloner.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Step;
import com.beantownloner.bakingapp.ui.RecipeDetailFragment;
import com.beantownloner.bakingapp.ui.RecipeStepDetailFragment;
import com.beantownloner.bakingapp.ui.VideoFragment;


import java.util.ArrayList;
import java.util.List;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.StepActionListener {

    private List<Step> steps;
    private Step step;
    private int stepID;
    private int lastStepID;
    private String recipeName;
    private static String TAG = RecipeStepDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Intent intent = getIntent();
        if (savedInstanceState != null) {

            step = savedInstanceState.getParcelable("step");
            steps = savedInstanceState.getParcelableArrayList("steps");
            stepID = step.getStepId();
            recipeName = savedInstanceState.getString("recipeName");
            Log.d(TAG, "in onCreate " + step.getStepId());
        } else if (intent != null) {
            Log.d(TAG, "intent is not null ");
            if (intent.getExtras() != null) {

                steps = getIntent().getParcelableArrayListExtra("steps");
                stepID = getIntent().getIntExtra("stepid", 0);
                recipeName = getIntent().getStringExtra("recipename");
            }
        }
        if (steps != null) {
            step = steps.get(stepID);
            lastStepID = steps.size() - 1;

        }
        Log.d(TAG, "recipeName = " + recipeName);

        setTitle(getString(R.string.title_activity_recipe_step_detail, recipeName));
        if (isLandscape()) {
            getSupportActionBar().hide();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            VideoFragment f = new VideoFragment();
            f.setStep(step);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_content, f)
                    .commit();

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RecipeStepDetailFragment f = new RecipeStepDetailFragment();
            f.setStep(step);
            f.setLastStepID(lastStepID);
            f.setRecipeName(recipeName);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_content, f)
                    .commit();

        }
    }

    @Override
    public void onNext() {

        if (stepID == steps.size() - 1) {

            stepID = 0;
        } else {

            stepID++;
        }
        step = steps.get(stepID);
        replaceFragment(step);


    }

    @Override
    public void onPrev() {

        if (stepID == 0) {

            stepID = steps.size() - 1;
        } else {

            stepID--;
        }
        step = steps.get(stepID);
        replaceFragment(step);

    }

    public void replaceFragment(Step newStep) {

        RecipeStepDetailFragment f = new RecipeStepDetailFragment();
        f.setStep(newStep);
        f.setLastStepID(lastStepID);
        f.setRecipeName(recipeName);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.recipe_step_detail_fragment_content, f)
                .commit();

    }

    private boolean isLandscape() {

        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "in onSaveInstanceState " + step.getStepId());
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", step);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);
        outState.putString("recipename", recipeName);
    }


}
