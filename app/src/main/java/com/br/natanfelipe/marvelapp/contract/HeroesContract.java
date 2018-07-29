package com.br.natanfelipe.marvelapp.contract;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.br.natanfelipe.marvelapp.model.Characters;

import java.util.List;

public interface HeroesContract {

    interface view{
        void showProgressBar();
        void hideProgressBar();
        void getErrorMessage(int statusCode);
        void displayData(List<Characters> charactersList);
        int getOffset();
    }

    interface presenter{
        void getRetrofitConnection();
        boolean hasInternetConnection(Context context);
        void getHeroes(Context context, boolean isGettingMore);
        int getTotal();
        void addRecyclerViewAnimation(RecyclerView recyclerView, Context context);
        void getHeroDetails(Context context, Characters characters);
        void openWifiSettings(Context context);
        void displayError(RecyclerView recyclerView, Context context, String code, boolean hasAction);
        void refreshData(SwipeRefreshLayout srl);
    }
}
