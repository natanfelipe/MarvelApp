package com.br.natanfelipe.marvelapp.contract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.ComicCharacters;
import com.br.natanfelipe.marvelapp.model.ComicItem;
import com.br.natanfelipe.marvelapp.model.Comics;

import java.util.List;

public interface ComicsContract {

    interface view{
        int getIdCharacter();
        void showProgressBar();
        void hideProgressBar();
        void getErrorMessage(int statusCode);
        void displayData(List<Characters> comicsList);

    }

    interface presenter{
        void getRetrofitConnection();
        boolean hasInternetConnection(Context context);
        void getComics(Context context);
        void addRecyclerViewAnimation(RecyclerView recyclerView, Context context);
        void getHeroDetails(Context context, Characters characters);
        void openWifiSettings(Context context);
        void displayError(RecyclerView recyclerView, Context context, String code, boolean hasAction);
    };
}
