package com.beantownloner.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = IngredientDetailFragment.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    @BindView(R.id.ingredients_tv)
    TextView ingredientTV;
    @BindView(R.id.ingredients_title_layout)
    LinearLayout titleLayout;
    private Unbinder unbinder;
    private List<Ingredient> ingredients;
    private OnFragmentInteractionListener mListener;


    private String recipeName;

    public IngredientDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientDetailFragment newInstance(String param1, String param2) {
        IngredientDetailFragment fragment = new IngredientDetailFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            ingredients = getArguments().getParcelableArrayList("ingredients");
            recipeName = getArguments().getString("recipename");
            Log.d(TAG, " recipeName = " + recipeName);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredient_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        ingredientTV.setText("Ingredients for " + recipeName);
        RecyclerView ingredientRecyclerVIew = (RecyclerView) rootView.findViewById(R.id.recipeIngredientsRV);
        ingredientRecyclerVIew.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecipeIngredientAdapter mAdapter2 = new RecipeIngredientAdapter();
        mAdapter2.swapData(ingredients);
        ingredientRecyclerVIew.setAdapter(mAdapter2);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // throw new RuntimeException(context.toString()
            //       + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);

    }

}
