package com.beantownloner.bakingapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.beantownloner.bakingapp.api.RecipeAPIClient;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.ui.RecipeAdapter;
import com.beantownloner.bakingapp.utilities.SharedPreferenceUtil;
import com.beantownloner.bakingapp.utilities.SimpleIdlingResource;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Pair<List<Recipe>, Exception>>,
        RecipeAdapter.RecipeListItemClickListener {

    private static final int ONLINE_RECIPE_LOADER_ID = 999;
    private static final int PHONE_POTRAIT_COLUMN = 1;
    private static final int PHONE_LANDSCAPE_COLUMN = 2;
    private static final int TABLET_POTRAIT_COLUMN = 2;
    private static final int TABLET_LANDSCAPE_COLUMN = 3;


    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;
    @BindView(R.id.recyclerview_recipe)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Context context = this;
        int numColumn = getColumn();
        GridLayoutManager layoutManager = new GridLayoutManager(context, numColumn);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(recipeAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("recipes")) {
                List<Recipe> recipes = savedInstanceState.getParcelableArrayList("recipes");
                recipeAdapter.swapData(recipes);
            }
            hideProgressBar();
        } else {
            getSupportLoaderManager().initLoader(ONLINE_RECIPE_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeAdapter != null) {
            List<Recipe> recipes = recipeAdapter.getData();
            if (recipes != null && !recipes.isEmpty()) {
                outState.putParcelableArrayList("recipes", new ArrayList<>(recipes));
            }
        }
    }

    @NonNull
    @Override
    public Loader<Pair<List<Recipe>, Exception>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Pair<List<Recipe>, Exception>>(this) {

            @Override
            protected void onStartLoading() {
                showProgressBar();
                forceLoad();
            }

            @Override
            public Pair<List<Recipe>, Exception> loadInBackground() {
                Call<List<Recipe>> call = RecipeAPIClient.getInstance().getRecipes();
                try {
                    Response<List<Recipe>> response = call.execute();
                    if (response != null && response.isSuccessful()) {
                        List<Recipe> recipes = response.body();
                        return new Pair<>(recipes, null);
                    }
                } catch (IOException ex) {

                    return new Pair<List<Recipe>, Exception>(null, ex);
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Pair<List<Recipe>, Exception>> loader, Pair<List<Recipe>, Exception> data) {
        hideProgressBar();
        if (data != null) {
            if (data.second != null) {
                showMessage(getErrorMessage(data.second));
            } else {
                final List<Recipe> recipes = data.first;
                recipeAdapter.swapData(recipes);

                SharedPreferenceUtil.saveRecipe(this, recipes.get(0));
                updateWidget();


            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Pair<List<Recipe>, Exception>> loader) {

    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {

        SharedPreferenceUtil.saveRecipe(this, recipe);
        updateWidget();

        Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    private String getErrorMessage(Exception ex) {
        if (ex instanceof UnknownHostException) {
            return getString(R.string.unknown_host_exception);
        } else {
            return ex.getMessage();
        }
    }

   private void showMessage(@NonNull String message) {
        Snackbar snackbar = Snackbar
                .make(layoutRoot, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    private int getColumn() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isTablet) {
            return isLandscape ? TABLET_LANDSCAPE_COLUMN : TABLET_POTRAIT_COLUMN;
        } else {
            return isLandscape ? PHONE_LANDSCAPE_COLUMN : PHONE_POTRAIT_COLUMN;
        }
    }

    public void updateWidget() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                // this will send the broadcast to update the appwidget
                BakingAppWidgetPrvider.sendRefreshBroadcast(MainActivity.this);
            }
        });

    }

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
