package com.beantownloner.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.*;

import com.beantownloner.bakingapp.objects.Recipe;
import com.google.gson.Gson;


public class SharedPreferenceUtil {
    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_name";
    public static final int NO_ID = -1;
    public static final String NO_NAME = "";


    public static void saveRecipe(@NonNull Context context, Recipe recipe) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefsEditor.putString("recipe", json);
        prefsEditor.commit();
    }


    public static final Recipe getRecipe(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipe", "");
        Recipe obj = gson.fromJson(json, Recipe.class);
        return obj;
    }

}