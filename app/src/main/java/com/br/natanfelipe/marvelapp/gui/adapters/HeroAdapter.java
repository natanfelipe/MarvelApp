package com.br.natanfelipe.marvelapp.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.natanfelipe.marvelapp.R;
import com.br.natanfelipe.marvelapp.interfaces.OnItemClickListener;
import com.br.natanfelipe.marvelapp.model.Characters;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.ViewHolder> implements Filterable {


    private List<Characters> characterList;
    private List<Characters> mFilteredList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public HeroAdapter(List<Characters> characterList,Context context, OnItemClickListener onItemClickListener) {
        this.characterList = characterList;
        this.mFilteredList = characterList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_hero)
        ImageView ivHero;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.cardView)
        CardView cardView;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void bind(final Characters character, final OnItemClickListener onItemClickListener) {
            loadHeroesData(character);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(character);
                }
            });
        }

        private void loadHeroesData(Characters character) {
            String url = character.getThumbnail().getPath() + "." + character.getThumbnail().getExtension();

            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivHero);

            tvName.setText(character.getName());

            cardView.setVisibility(View.VISIBLE);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hero_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(mFilteredList.get(position),onItemClickListener);

    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String text = charSequence.toString();
                if(text.isEmpty())
                    mFilteredList = characterList;
                else {
                    mFilteredList = new ArrayList<>();
                    for(Characters c: characterList){
                        String country = c.getName().toLowerCase();
                        if(country.contains(text)){
                            mFilteredList.add(c);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = mFilteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Characters>) filterResults.values;
                if(characterList.size() <= 20)
                  notifyDataSetChanged();

            }
        };
    }
}
