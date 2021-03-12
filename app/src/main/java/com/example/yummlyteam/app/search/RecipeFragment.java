package com.example.yummlyteam.app.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yummlyteam.app.adapter.RecipeListAdapter;
import com.example.yummlyteam.app.model.Match;
import com.example.yummlyteam.app.model.RecipeSearchList;
import com.example.yummlyteam.yummly_project.R;

import java.util.ArrayList;


public class RecipeFragment extends Fragment {
    private static final String TAG = RecipeFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecipeViewModel mViewModel;

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecipeListAdapter(new ArrayList<Match>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel.nextSearchPage();
                }
            }
        });

        // Create the observer which updates the UI.
        final Observer<RecipeSearchList> searchListObserver = new Observer<RecipeSearchList>() {
            @Override
            public void onChanged(@Nullable final RecipeSearchList searchList) {
                // Update the UI
                if (searchList == null || searchList.getMatches() == null) { // clear the list
                    ((RecipeListAdapter) recyclerView.getAdapter()).clearList();
                } else {
                    ((RecipeListAdapter) recyclerView.getAdapter()).addItems(searchList.getMatches());
                }
            }
        };
        // attach the observer
        mViewModel.getSearchList().observe(getViewLifecycleOwner(), searchListObserver);
    }
}
