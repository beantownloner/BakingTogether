package com.beantownloner.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.ui.IngredientDetailFragment;
import com.beantownloner.bakingapp.ui.RecipeStepDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class IngredientsDetailActivity extends AppCompatActivity {
    private List<Ingredient> ingredients;
    private String recipeName;
    public static String TAG = IngredientsDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_detail);

        Intent intent = getIntent();

        if (intent != null) {

            if (intent.getExtras() != null) {

                ingredients = getIntent().getParcelableArrayListExtra("ingredients");
                recipeName = getIntent().getStringExtra("recipename");
                if (ingredients != null) {
                    Log.d(TAG, "ingredients size " + ingredients.size());

                } else {

                    Log.d(TAG, " Ingredients are null ");
                }
            }
        }

        setTitle(getString(R.string.ingredient_of, recipeName));
        IngredientDetailFragment f = new IngredientDetailFragment();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredients);
        bundle.putString("recipename", recipeName);
        f.setArguments(bundle);
        fm.beginTransaction()
                .add(R.id.ingredients_detail_fragment_content, f)
                .commit();

    }
}
