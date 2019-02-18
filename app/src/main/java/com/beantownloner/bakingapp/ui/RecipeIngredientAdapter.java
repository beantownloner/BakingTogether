package com.beantownloner.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Ingredient;
import com.beantownloner.bakingapp.objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredients;

    @NonNull
    @Override
    public RecipeIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.recipe_ingredient_item, parent, false);
        return new RecipeIngredientAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    public void swapData(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public List<Ingredient> getData() {
        return ingredients;
    }


    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientTV)
        TextView ingredientName;

        @BindView(R.id.quantityTV)
        TextView ingredientQuantity;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(@NonNull Ingredient ingredient) {

            ingredientName.setText(ingredient.getName() + "");
            ingredientQuantity.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
        }


    }
}
