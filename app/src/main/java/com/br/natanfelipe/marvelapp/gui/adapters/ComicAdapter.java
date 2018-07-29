package com.br.natanfelipe.marvelapp.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.interfaces.OnItemClickListener;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.br.natanfelipe.marvelapp.model.ComicItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {


    private List<Characters> comicsList;
    private Context context;

    public ComicAdapter(List<Characters> comicsList, Context context) {
        this.comicsList =  comicsList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_hero)
        ImageView ivHero;
        @BindView(R.id.cardView)
        CardView cardView;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void bind(final Characters comics) {
            String url = comics.getThumbnail().getPath() + "." + comics.getThumbnail().getExtension();

            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivHero);
            cardView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comics_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(comicsList.get(position));

    }

    @Override
    public int getItemCount() {
        return comicsList.size();
    }
}
