package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Serie;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.ComicCardAdapter;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.utils.Singleton;
import jcollado.pw.pimpam.widgets.GridSpacingItemDecoration;


public class CollectionFragment extends BaseFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;

    private CollectionFragment.OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private ComicCardAdapter adapter;
    private List<Comic> comicList;
    SearchView searchView = null;
    MenuItem myActionMenuItem;
    public CollectionFragment() {
        // Required empty public constructor
    }


    public static CollectionFragment newInstance() {
        CollectionFragment fragment = new CollectionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =     inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        initCollapsingToolbar(view);
        prepareToolbar();
        prepareRecyclerView(view);
        prepareComics();
        listenerSwipeLayout();

        try {
            //Fondo de la CollapsingToolbar
           // Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view ;

    }
    private void prepareToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.hamburger));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.openDrawer();
            }
        });

        toolbar.setTitle(getString(R.string.seeCollection));
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.seeCollection));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few comics for testing
     */
    private void prepareComics() {

        //onPreStartConnection(getString(R.string.loading));

        ArrayList<Serie> series = Singleton.getInstance().getDatabase().getSeries();
        ArrayList<Comic> comics = Singleton.getInstance().getDatabase().getComics();

        for (Comic comic : comics){
            comicList.add(comic);
        }

        adapter.notifyDataSetChanged();


    }
    private void prepareRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        comicList = new ArrayList<>();
        adapter = new ComicCardAdapter(getActivity(), comicList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Functions.dpToPx(10,getActivity().getApplicationContext()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

        private void listenerSwipeLayout(){

            refreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new LoadBackgroundTask().execute();

                        }
                    }
            );
            refreshLayout.setColorSchemeResources(
                    R.color.accent

            );
        }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddComicFragment.OnFragmentInteractionListener) {
            mListener = (CollectionFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void stopRefreshing() {

        onConnectionFinished();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate( R.menu.collection_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

         myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.searchToolbarHint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Log.i("search","SearchOnQueryTextSubmit: " + query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
    }

    private class LoadBackgroundTask extends AsyncTask<Void, Void, List<Comic>> {

        static final int DURACION = 3 * 1000 ; // 3 segundos de carga

        @Override
        protected List doInBackground(Void... params) {
            // Simulación de la carga de items
            try {
                Thread.sleep(DURACION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Retornar en nuevos elementos para el adaptador
            return Singleton.getInstance().getDatabase().getComics();
        }

        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);

            // Limpiar elementos antiguos
            adapter.clear();

            // Añadir elementos nuevos
            adapter.addAll(result);

            // Parar la animación del indicador
            refreshLayout.setRefreshing(false);
        }

    }
}
