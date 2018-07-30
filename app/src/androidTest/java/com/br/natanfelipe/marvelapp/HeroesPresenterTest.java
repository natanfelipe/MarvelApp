package com.br.natanfelipe.marvelapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.UiThread;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.br.natanfelipe.marvelapp.connections.ApiInterface;
import com.br.natanfelipe.marvelapp.connections.RestApiKey;
import com.br.natanfelipe.marvelapp.contract.HeroesContract;
import com.br.natanfelipe.marvelapp.gui.adapters.HeroAdapter;
import com.br.natanfelipe.marvelapp.gui.view.HeroDetailActivity;
import com.br.natanfelipe.marvelapp.interfaces.OnItemClickListener;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.ComicItem;
import com.br.natanfelipe.marvelapp.model.Data;
import com.br.natanfelipe.marvelapp.model.Info;
import com.br.natanfelipe.marvelapp.presenter.HeroesPresenter;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class HeroesPresenterTest extends TestCase{

    @InjectMocks
    private HeroesPresenter presenter;

    @Mock
    private HeroesContract.view view;

    @Mock
    private ApiInterface apiInterface;


    RestApiKey restApiKey;

    @Mock
    HeroAdapter heroAdapter;

    Info heroesResponse;

    @Mock
    OnItemClickListener onItemClickListener;

    @Mock
    Context context;

    HeroAdapter.ViewHolder holder;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter.getRetrofitConnection();
        restApiKey = new RestApiKey();
        apiInterface = restApiKey.configure();
    }

    /*@Test
    public void deviceOffline() throws Exception {
        presenter.hasInternetConnection(context);
        presenter.openWifiSettings(context);
    }*/

   /* @Test
   public void loadDataSuccessfully(){
        restApiKey = new RestApiKey();
        restApiKey.configure();

        presenter.getHeroes(context,false);


        Mockito.verify(apiInterface).charactersList("1",BuildConfig.API_KEY,BuildConfig.HASH)
               .enqueue(mCallbackArgumentCaptor.capture());

   }*/

    @Test
    public void loadHeroDataSuccessfully() {


        Call<Info> call = apiInterface.charactersList("1",BuildConfig.API_KEY,BuildConfig.HASH);

        try {
            Response<Info> response = call.execute();

            assertTrue(response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadHeroDataError() {

        Call<Info> call = apiInterface.charactersList("hey",BuildConfig.API_KEY,BuildConfig.HASH);

        try {
            Response<Info> response = call.execute();

            assertTrue(!response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void loadComicsDataSuccessfully() {


        Call<Info> call = apiInterface.comicsList(1011334,"1",BuildConfig.API_KEY,BuildConfig.HASH);

        try {
            Response<Info> response = call.execute();

            assertTrue(response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadComicsDataError() {

        Call<Info> call = apiInterface.comicsList(1,"hey",BuildConfig.API_KEY,BuildConfig.HASH);

        try {
            Response<Info> response = call.execute();

            assertTrue(!response.isSuccessful());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void HeroViewHolder() {


        Call<Info> call = apiInterface.charactersList("1",BuildConfig.API_KEY,BuildConfig.HASH);

        try {
            Response<Info> response = call.execute();

            heroesResponse = response.body();

            heroAdapter = new HeroAdapter(heroesResponse.getData().getResults(),context,onItemClickListener);

            View listItemView = LayoutInflater.from(context).inflate(R.layout.item_hero_layout, null, false);
            holder = new HeroAdapter.ViewHolder(listItemView);
            heroAdapter.onBindViewHolder(holder, 0);
            final Characters character = heroesResponse.getData().getResults().get(0);
            holder.bind(heroesResponse.getData().getResults().get(0),onItemClickListener);

            assertEquals("3-D Man",character.getName().toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ComicItem> comicsList = character.getComics().getItems();
                    presenter.getHeroDetails(context,character);

                    Intent intent = new Intent(context,HeroDetailActivity.class);
                    intent.putParcelableArrayListExtra("comics", (ArrayList<? extends Parcelable>) comicsList);
                    Mockito.verify(context).startActivity(intent);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
