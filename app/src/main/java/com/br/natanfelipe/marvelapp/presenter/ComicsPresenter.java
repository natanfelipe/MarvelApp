package com.br.natanfelipe.marvelapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.br.natanfelipe.marvelapp.BuildConfig;
import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.connections.RestApiKey;
import com.br.natanfelipe.marvelapp.contract.ComicsContract;
import com.br.natanfelipe.marvelapp.gui.view.HeroDetailActivity;
import com.br.natanfelipe.marvelapp.interfaces.ServerResponseConnector;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.model.Info;
import com.br.natanfelipe.marvelapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ComicsPresenter implements ComicsContract.presenter, ServerResponseConnector {

    RestApiKey task;
    ComicsContract.view view;
    List<Characters> comicsList = new ArrayList<>();

    public ComicsPresenter(ComicsContract.view view) {
        this.view = view;
    }


    @Override
    public void getRetrofitConnection() {
        task = new RestApiKey();
        task.setServerResponseConnector(this);
    }


    @Override
    public boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void getComics(Context context) {
        if(hasInternetConnection(context))
            new ComicsData().execute();
        else
            view.getErrorMessage(Constants.NO_CONNECTION_ERROR);
    }



    private class ComicsData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            task.comicsList(view.getIdCharacter(),"1", BuildConfig.API_KEY,BuildConfig.HASH);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.hideProgressBar();
            view.displayData(comicsList);
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
            Intent intent = new Intent(contexto, HeroDetailActivity.class);
            intent.putExtra("name",characters.getName());
            intent.putExtra("description",characters.getDescription());
            intent.putExtra("thumbnail",thumbnail);
            intent.putExtra("comics",comics);
            intent.putExtra("id",characters.getId());
            contexto.startActivity(intent);


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

                if(responseData != null && ((Info) responseData).getData().getResults() != null) {
                    final List<Characters> comicResponse = ((Info) responseData).getData().getResults();
                    for (final Characters comicItem : comicResponse) {
                        comicsList.add(comicItem);
                    }
                    Log.d("TESTE", "onConnectionResult: "+comicsList.size());
                }

            }
        }
    }
}
