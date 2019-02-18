package com.beantownloner.bakingapp.service;


import android.content.Intent;
import android.widget.RemoteViewsService;


public class IngredientListWidgetService extends RemoteViewsService {
    private static String TAG = IngredientListWidgetService.class.getSimpleName();


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientListRemoteViewsFactory(this.getApplicationContext());
    }
}

