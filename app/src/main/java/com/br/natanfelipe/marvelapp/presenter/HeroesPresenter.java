package com.br.natanfelipe.marvelapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.br.natanfelipe.marvelapp.BuildConfig;
import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.connections.RestApiKey;
import com.br.natanfelipe.marvelapp.contract.HeroesContract;
import com.br.natanfelipe.marvelapp.gui.adapters.HeroAdapter;
import com.br.natanfelipe.marvelapp.gui.view.HeroDetailActivity;
import com.br.natanfelipe.marvelapp.interfaces.ServerResponseConnector;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.model.Info;
import com.br.natanfelipe.marvelapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HeroesPresenter implements HeroesContract.presenter, ServerResponseConnector {

    RestApiKey task;
    HeroesContract.view view;
    List<Characters> charactersList = new ArrayList<>();
    int total;
    boolean isFirstTime = true;

    public HeroesPresenter(HeroesContract.view view) {
        this.view = view;
    }


    @Override
    public void getRetrofitConnection() {
        task = new RestApiKey();
        task.setServerResponseConnector(this);
    }


    @Override
    public boolean hasInternetConnection(Context context) {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void getHeroes(Context context, boolean isGettingMore) {
        if(hasInternetConnection(context))
            new HeroesData().execute();
        else
            view.getErrorMessage(Constants.NO_CONNECTION_ERROR);
    }



    public class HeroesData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(isFirstTime)
             task.heroesList("1", BuildConfig.API_KEY,BuildConfig.HASH);
            else
             task.loadMoreHeroes(view.getOffset(),"1", BuildConfig.API_KEY,BuildConfig.HASH);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.hideProgressBar();
            view.displayData(charactersList);
        }
    }


    @Override
    public void addRecyclerViewAnimation(RecyclerView recyclerView, Context context) {
        int resId = R.anim.layout_animation;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);
    }

    @Override
    public void getHeroDetails(Context contexto, Characters characters) {

            Comics comics = characters.getComics();
            String thumbnail = characters.getThumbnail().getPath()+"."+characters.getThumbnail().getExtension();
            if(comics.getItems().size()>0){
                Intent intent = new Intent(contexto, HeroDetailActivity.class);
                intent.putExtra("name",characters.getName());
                intent.putExtra("thumbnail",thumbnail);
                intent.putExtra("comics",comics);
                intent.putExtra("id",characters.getId());
                contexto.startActivity(intent);
            } else {
                Toast.makeText(contexto, contexto.getResources().getText(R.string.no_comics), Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void openWifiSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

    }

    @Override
    public void displayError(RecyclerView recyclerView, final Context context, String message, boolean hasAction) {
        if(hasAction) {
            Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(context.getResources().getText(R.string.bt_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openWifiSettings(context);
                        }
                    })
                    .show();
        } else {
            Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(context.getResources().getText(R.string.bt_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .show();
        }
    }

    @Override
    public void refreshData(SwipeRefreshLayout srl) {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstTime = true;
                new HeroesData().execute();
            }
        });
    }

    @Override
    public void onConnectionResult(int statusCode, Object responseData) {

        if (statusCode == Constants.NO_CONNECTION_ERROR) {
            view.getErrorMessage(Constants.NO_CONNECTION_ERROR);
            return;
        }

        if (statusCode == Constants.GENERIC_RESPONSE_CODE_ERROR
                || statusCode == Constants.FORBIDDEN) {
            view.getErrorMessage(Constants.GENERIC_RESPONSE_CODE_ERROR);
            return;
        }
        if (statusCode == Constants.NOT_FOUND) {
            view.getErrorMessage(Constants.NOT_FOUND);
        }
        if (statusCode == 200) {
            if (responseData instanceof Info) {
                if(isFirstTime) {
                    total = ((Info) responseData).getData().getTotal();
                    isFirstTime = false;
                }
                final List<Characters> charactersResponse = ((Info) responseData).getData().getResults();
                for (final Characters character : charactersResponse) {
                    charactersList.add(character);
                }
            }
        }
    }

    @Override
    public int getTotal() {
        return total;
    }
}
