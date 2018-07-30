package com.br.natanfelipe.marvelapp.gui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.contract.HeroesContract;
import com.br.natanfelipe.marvelapp.gui.adapters.HeroAdapter;
import com.br.natanfelipe.marvelapp.interfaces.OnItemClickListener;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.ComicItem;
import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.presenter.HeroesPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HeroesContract.view{

    private HeroesContract.presenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private Context context;

    private HeroAdapter adapter;

    int count;

    boolean isGettingMore = false;

    boolean isLoading = true;

    int previousTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        context = this;

        presenter = new HeroesPresenter(this);
        presenter.getRetrofitConnection();
        presenter.getHeroes(this,isGettingMore);
        presenter.refreshData(srl);

        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {

                    if(isLoading) {
                        if(totalItemCount > previousTotal){
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if(!isLoading && (totalItemCount - visibleItemCount)<=(lastVisible + 10)){
                        if(count <= presenter.getTotal()){
                            isGettingMore = true;
                            presenter.getHeroes(context,isGettingMore);
                        }

                        isLoading = true;
                    }
                }
            }
        });

    }

    @Override
    public void showProgressBar() {
        srl.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        srl.setRefreshing(false);
    }

    @Override
    public void getErrorMessage(int statusCode) {

    }

    @Override
    public void displayData(List<Characters> charactersList) {
        adapter = new HeroAdapter(charactersList, context, new OnItemClickListener() {
            @Override
            public void onItemClick(Characters characters) {

                List<ComicItem> comicsList = characters.getComics().getItems();
                presenter.getHeroDetails(context,characters);

                Intent intent = new Intent(context,HeroDetailActivity.class);
                intent.putParcelableArrayListExtra("comics", (ArrayList<? extends Parcelable>) comicsList);
                }
        });


        if(!isGettingMore) {
            recyclerView.setAdapter(adapter);
            presenter.addRecyclerViewAnimation(recyclerView,context);
        }
        else
          adapter.notifyItemRangeChanged(adapter.getItemCount(), charactersList.size() - 1);

        count = adapter.getItemCount();
    }

    @Override
    public int getOffset() {
        return count;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.layout_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        search(searchView);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
