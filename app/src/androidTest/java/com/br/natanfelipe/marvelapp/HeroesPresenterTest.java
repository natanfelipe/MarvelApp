package com.br.natanfelipe.marvelapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.br.natanfelipe.marvelapp.connections.ApiInterface;
import com.br.natanfelipe.marvelapp.contract.HeroesContract;
import com.br.natanfelipe.marvelapp.model.Characters;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class HeroesPresenterTest extends TestCase{

    @InjectMocks
    private HeroesPresenter presenter;

    @Mock
    private HeroesContract.view view;

    @Mock
    private ApiInterface apiInterface;

    @Mock
    private Object response;

    @Captor
    private ArgumentCaptor<Callback<Info>> mCallbackArgumentCaptor;


    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter.getRetrofitConnection();
    }

    /*@Test
    public void deviceOffline() throws Exception {
        presenter.hasInternetConnection(context);
        presenter.openWifiSettings(context);
    }*/

    @Test
   public void loadDataSuccessfully(){

        presenter.getHeroes(context,false);


        Mockito.verify(apiInterface).charactersList("1",BuildConfig.API_KEY,BuildConfig.HASH)
               .enqueue(mCallbackArgumentCaptor.capture());

   }

}
