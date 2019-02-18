package com.beantownloner.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.objects.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private final RecipeStepItemClickListener recipeStepItemClickListener;
    private List<Step> steps;
    private Context context;
    private boolean isCurrent;
    private boolean isTablet;
    private int currentPosition;

    public RecipeStepAdapter(RecipeStepItemClickListener recipeStepItemClickListener) {
        this.recipeStepItemClickListener = recipeStepItemClickListener;
        this.currentPosition = 0;
    }

    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.recipe_step_item, parent, false);

        return new RecipeStepAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecipeStepAdapter.ViewHolder holder, int position) {
        Step step = steps.get(position);
        if (position == currentPosition) {
            setCurrent(true);
        } else {
            setCurrent(false);
        }
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public void swapData(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public List<Step> getData() {
        return steps;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_id_tv)
        TextView stepID;

        @BindView(R.id.step_tv)
        TextView stepName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);


        }

        private void bind(@NonNull Step step) {

            stepID.setText(step.getStepId() + "");
            stepName.setText(step.getShortDescription());
            if (isCurrent && isTablet) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.colorHilight));
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);

            }
        }

        @Override
        public void onClick(View v) {
            recipeStepItemClickListener.onRecipeStepItemClick(getAdapterPosition());
        }
    }

    public interface RecipeStepItemClickListener {
        void onRecipeStepItemClick(int position);
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


}
