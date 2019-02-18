package com.beantownloner.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.service.IngredientListWidgetService;

import com.beantownloner.bakingapp.utilities.SharedPreferenceUtil;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetPrvider extends AppWidgetProvider {


    private static String TAG = BakingAppWidgetPrvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views;
        if (SharedPreferenceUtil.getRecipe(context) != null) {
            Log.d(TAG, " recipe is not empty");
            views = getIngredientListRemoteView(context);
        } else {
            Log.d(TAG, " recipe is  empty");
            //views = getEmptyRemoteView(context);
            views = getIngredientListRemoteView(context);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        updateRecipeWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getIngredientListRemoteView(Context context) {

        Recipe recipe = SharedPreferenceUtil.getRecipe(context);

        List<Ingredient> ingredients = recipe.getIngredients();

        if (ingredients != null) {

            Log.d(TAG, " ingredients size " + ingredients.size());
        } else {

            Log.d(TAG, "ingredients are null ");
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        String recipeName = recipe.getName();
        views.setTextViewText(R.id.title_text_view, context.getString(R.string.ingredient_of, recipeName));

        Intent intent = new Intent(context, IngredientListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);


        return views;
    }

    private static RemoteViews getEmptyRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.empty_widget_view);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.empty_widget_image_view, appPendingIntent);
        views.setOnClickPendingIntent(R.id.empty_widget_image_view, appPendingIntent);

        return views;
    }

    public static void sendRefreshBroadcast(Context context) {
        Log.d(TAG, " in sendRefreshBroadcast");
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, BakingAppWidgetPrvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, " in onReceive");
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BakingAppWidgetPrvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list_view);
            updateRecipeWidgets(context, mgr, mgr.getAppWidgetIds(cn));
        }
        super.onReceive(context, intent);
    }
}

