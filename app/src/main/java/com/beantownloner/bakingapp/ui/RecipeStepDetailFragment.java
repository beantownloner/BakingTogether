package com.beantownloner.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beantownloner.bakingapp.R;
import com.beantownloner.bakingapp.objects.Recipe;
import com.beantownloner.bakingapp.objects.Step;
import com.beantownloner.bakingapp.utilities.ImageUtil;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    private boolean isTablet;
    private static String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private int lastStepID;
    private Step step;
    private String mediaUrl;
    private TrackSelector trackSelector;
    private Unbinder unbinder;


    private String recipeName;
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.image_no_video)
    ImageView noVideoImage;

    @BindView(R.id.button_prev)
    Button prevButton;
    @BindView(R.id.button_nxt)
    Button nextButton;
    @BindView(R.id.recipe_step_title_tv)
    TextView stepTitle;
    @BindView(R.id.recipe_step_detail_tv)
    TextView recipeStepTV;
    private StepActionListener stepActionListener;


    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeStepDetailFragment newInstance(String param1, String param2) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable("step");

            Log.d(TAG, " saved step id " + step.getStepId());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable("step");

            Log.d(TAG, " saved step id in onCreateView " + step.getStepId());
        }


        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mediaUrl = step.getVideoURL();


        Log.d(TAG, " saved step id 2 = " + step.getStepId());
        stepTitle.setText(step.getShortDescription());
        if (isTablet) {
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);

        } else {
            if (step.getStepId() == 0) {

                prevButton.setVisibility(View.INVISIBLE);
            } else if (step.getStepId() == lastStepID) {

                nextButton.setVisibility(View.INVISIBLE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

            }

        }
        recipeStepTV.setText(step.getDescription());

        if (!TextUtils.isEmpty(mediaUrl)) {

            noVideoImage.setVisibility(View.GONE);
            initializePlayer();
        } else {

            if (!TextUtils.isEmpty(step.getThumbnailURL())) {
                mediaUrl = step.getThumbnailURL();
                noVideoImage.setVisibility(View.GONE);
                initializePlayer();
            } else {
                Picasso.with(getActivity()).load(ImageUtil.getImageResId(recipeName)).into(noVideoImage);
                noVideoImage.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
            }

        }
        return rootView;
    }

    private void initializePlayer() {
        trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

        playerView.setPlayer(player);


        Uri uri = Uri.parse(mediaUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
        player.seekTo(playbackPosition);
        player.setPlayWhenReady(playWhenReady);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepActionListener = (StepActionListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " should implements interface StepActionListener.");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @OnClick()
    public void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Optional
    @OnClick(R.id.button_prev)
    public void previousStep() {
        stepActionListener.onPrev();
    }

    @Optional
    @OnClick(R.id.button_nxt)
    public void nextStep() {
        stepActionListener.onNext();
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public interface StepActionListener {
        void onNext();

        void onPrev();
    }

    public int getLastStepID() {
        return lastStepID;
    }

    public void setLastStepID(int lastStepID) {
        this.lastStepID = lastStepID;
    }

    public interface RecipeStepItemClickListener {
        void onRecipeStepItemClick(int position);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Log.d(TAG, "in onSaveInstanceState " + step.getStepId());
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", step);

    }

    public boolean isTablet() {
        return isTablet;
    }
    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
