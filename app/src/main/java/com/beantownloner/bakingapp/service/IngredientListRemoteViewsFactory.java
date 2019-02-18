package com.beantownloner.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.RecipeDetailActivity;
import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.utilities.SharedPreferenceUtil;

class IngredientListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = IngredientListRemoteViewsFactory.class.getSimpleName();


    Context mContext;
    Recipe recipe;

    public IngredientListRemoteViewsFactory(Context applicationContext) {
        Log.d(TAG, " WIDGETTEST ");
        mContext = applicationContext;
        recipe = SharedPreferenceUtil.getRecipe(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        recipe = SharedPreferenceUtil.getRecipe(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe != null && recipe.getIngredients() != null ?
                recipe.getIngredients().size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = recipe.getIngredients().get(position);

        if (ingredient == null) return null;

        Log.d(TAG, " WIDGETTEST " + ingredient.getName());
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        views.setTextViewText(R.id.textview_ingredient_summary, ingredient.toString());
        Bundle extras = new Bundle();
        extras.putParcelable("recipe", recipe);
        Intent fillInIntent = new Intent(mContext, RecipeDetailActivity.class);
        fillInIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.textview_ingredient_summary, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}