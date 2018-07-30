package com.br.natanfelipe.marvelapp.gui.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.contract.ComicsContract;
import com.br.natanfelipe.marvelapp.gui.adapters.ComicAdapter;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.ComicItem;
import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.presenter.ComicsPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeroDetailActivity extends AppCompatActivity implements ComicsContract.view{

    Comics comics;
    String thumbnail;
    String name;
    int id;

    private ComicAdapter adapter;


    @BindView(R.id.img)
    ImageView imageView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.ct)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ComicsContract.presenter presenter;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        context = this;

        presenter = new ComicsPresenter(this);
        presenter.getRetrofitConnection();

        if(intent.getParcelableExtra("comics") != null)
         comics = getIntent().getParcelableExtra("comics");
        if(intent.getStringExtra("thumbnail") != null) {
            thumbnail = getIntent().getStringExtra("thumbnail");
            Picasso.get()
                    .load(thumbnail)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        }
        if(intent.hasExtra("name")) {
            name = getIntent().getStringExtra("name");
            collapsingToolbarLayout.setTitle(name);
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        id = intent.getIntExtra("id",0);

        presenter.getComics(this);

        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public int getIdCharacter() {
        return id;
    }

    @Override
    public void showProgressBar() {
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getErrorMessage(int statusCode) {

    }


    @Override
    public void displayData(List<Characters> comicsList) {
        adapter = new ComicAdapter(comicsList, context);
        recyclerView.setAdapter(adapter);
        presenter.addRecyclerViewAnimation(recyclerView,context);
    }
}
