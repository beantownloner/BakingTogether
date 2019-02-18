package com.beantownloner.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beantownloner.bakingapp.IngredientsDetailActivity;
import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.RecipeStepDetailActivity;
import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.objects.Step;
import com.beantownloner.bakingapp.utilities.SharedPreferenceUtil;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment
        implements RecipeStepDetailFragment.RecipeStepItemClickListener, RecipeStepAdapter.RecipeStepItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Recipe recipe;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    public RecyclerView recyclerView;
    private StepClickListener stepClickListener;
    private IngredientClickListener ingredientClickListener;
    private boolean isTablet;
    private static String TAG = RecipeDetailFragment.class.getSimpleName();
    private RecipeStepAdapter mAdapter;
    private int currentStepPosition;
    @BindView(R.id.ingredientsTV)
    TextView ingredientTV;
    @BindView(R.id.recipe_detail_rv)
    ScrollView scrollView;
    @BindView(R.id.ingredients_card_view)
    CardView ingredientCardView;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            recipe = getArguments().getParcelable("recipe");

            if (recipe != null) {

                steps = recipe.getSteps();
                ingredients = recipe.getIngredients();
            }


            SharedPreferenceUtil.saveRecipe(getActivity(), recipe);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recipeStepsRV);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d(TAG, " currentPosition " + currentStepPosition);
        mAdapter = new RecipeStepAdapter(this);
        mAdapter.setCurrentPosition(currentStepPosition);
        mAdapter.setTablet(isTablet());
        mAdapter.swapData(steps);

        String ingredientTitle = "Ingredients for " + recipe.getName();
        ingredientTV.setText(ingredientTitle);
        if (currentStepPosition == -1 && isTablet()) {

            ingredientCardView.setBackgroundColor(getResources().getColor(R.color.colorHilight));
        } else {
            ingredientCardView.setBackgroundColor(Color.WHITE);

        }

        recyclerView.setAdapter(mAdapter);
        ingredientTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientClickListener.onIngredientClicked();
            }
        });

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });


        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepClickListener = (StepClickListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " should implements interface StepClickListener");
        }
        try {
            ingredientClickListener = (IngredientClickListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " should implements interface ingredientClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        stepClickListener = null;
        ingredientClickListener = null;
    }

    @Override
    public void onRecipeStepItemClick(int position) {
        stepClickListener.onStepClicked(position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface StepClickListener {
        void onStepClicked(int position);
    }

    public interface IngredientClickListener {

        void onIngredientClicked();
    }

    public int getCurrentStepPosition() {
        return currentStepPosition;
    }

    public void setCurrentStepPosition(int currentStepPosition) {
        this.currentStepPosition = currentStepPosition;
    }

    public void changeCurrentItemView(int currentStepPosition) {
        this.currentStepPosition = currentStepPosition;

        if (currentStepPosition == -1) {

            ingredientCardView.setBackgroundColor(getResources().getColor(R.color.colorHilight));

        } else {
            ingredientCardView.setBackgroundColor(Color.WHITE);
        }

        mAdapter.setCurrentPosition(currentStepPosition);
        mAdapter.notifyDataSetChanged();

    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);

    }


}
