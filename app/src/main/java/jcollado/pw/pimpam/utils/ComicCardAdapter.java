package jcollado.pw.pimpam.utils;

/**
 * Created by colla on 28/03/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.controller.MainActivity;
import jcollado.pw.pimpam.controller.ViewComicFragment;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;


public class ComicCardAdapter extends RecyclerView.Adapter<ComicCardAdapter.CardHolder> {

    private Context mContext;
    private List<Comic> comicList;
    private Database database;

    public ComicCardAdapter(Context mContext, List<Comic> comicList, Database database) {
        this.mContext = mContext;
        this.comicList = comicList;
        this.database = database;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_card, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        final Comic comic = comicList.get(position);
        holder.title.setText(comic.getDisplayName());
        holder.count.setText(comic.getEditorial());

        // loading album cover using Glide library
        Glide.with(mContext).load(comic.getImageURL()).placeholder(R.drawable.placeholder).into(holder.thumbnail);

        holder.favButton.setChecked(comic.getFavourite());


        holder.favButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                        comic.setFavourite(true);
                         Database.getInstance().comicToDatabase(comic,null);


                } else {
                        comic.setFavourite(false);
                      Database.getInstance().comicToDatabase(comic,null);

                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openViewComicFragment(comicList.get(position));

            }
        });
    }
    private void openViewComicFragment(Comic comic){
        ViewComicFragment fragment = ViewComicFragment.newInstance();
        fragment.setComic(comic);
        MainActivity myActivity = (MainActivity) mContext;
         myActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment,"")
                .commit();
    }

    /*
        Añade una lista completa de items
      */
    public void addAll(List<Comic> lista){
        clear();
        comicList.addAll(lista);
        notifyDataSetChanged();
    }

    /*
    Permite limpiar todos los elementos del recycler
     */
    public void clear(){
        comicList.clear();
        notifyDataSetChanged();
    }




    public class CardHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public TextView title, count;
        public ImageView thumbnail;
        public SparkButton favButton;

        public CardHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            favButton = (SparkButton) view.findViewById(R.id.star_button1);


            view.setOnClickListener(this);


        }

        @Override
        //onClick method when click a card
        public void onClick(View view) {
            openViewComicFragment(comicList.get(getAdapterPosition()));
        }
    }

}