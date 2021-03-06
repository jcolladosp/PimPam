package jcollado.pw.pimpam.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jcollado.pw.pimpam.R;
import jcollado.pw.pimpam.model.Comic;
import jcollado.pw.pimpam.model.Database;
import jcollado.pw.pimpam.utils.BaseFragment;
import jcollado.pw.pimpam.utils.ComicCardAdapter;
import jcollado.pw.pimpam.utils.Functions;
import jcollado.pw.pimpam.widgets.GridSpacingItemDecoration;


public class CollectionFragment extends BaseFragment {
    public static final int PLACE_IN_DRAWER = 1;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.addComic)    FloatingActionButton addComic;
    @BindView(R.id.noComicsTX)
    TextView noComicsTX;
    @BindView(R.id.noResultsTX)
    TextView noResultsTX;

    private RecyclerView recyclerView;
    private ComicCardAdapter adapter;
    private List<Comic> comicList;
    SearchView searchView = null;
    MenuItem myActionMenuItem;
    public boolean isAtached = false;
    AddComicFragment addComicFragment = new AddComicFragment() ;
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
        Database.getInstance().setFragment(this);
        setHasOptionsMenu(true);
        if(Database.getInstance().isFirstLoad()) {
            mostrarCargando();
            Database.getInstance().setFirstLoad(false);
        }
        initCollapsingToolbar(view);
        prepareToolbar();
        prepareRecyclerView(view);
        prepareComics();



            //Fondo de la CollapsingToolbar
           // Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));



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
    public void prepareComics() {
        recyclerView.setVisibility(View.VISIBLE);
        noResultsTX.setVisibility(View.GONE);
        adapter.clear();

        ArrayList<Comic> comics = Database.getInstance().getComics();
        if (comics.size() == 0 ) {
            noComicsTX.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            for (Comic comic : comics) {
                comicList.add(comic);
            }
            noComicsTX.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
        adapter.notifyDataSetChanged();


    }
    private void prepareRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        comicList = new ArrayList<>();
        adapter = new ComicCardAdapter(getActivity(), comicList,Database.getInstance());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Functions.dpToPx(10,getActivity().getApplicationContext()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }




    @Override
    public void onAttach(Context context) {
        isAtached = true;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        isAtached = false;
        super.onDetach();
    }


    public void stopRefreshing() {

        onConnectionFinished();
    }
    public void mostrarCargando(){
        onPreStartConnection();
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
                ArrayList<Comic> comicsFiltered = new ArrayList<>();
                for(Comic comic : comicList){
                    String displayName = comic.getDisplayName().toLowerCase();
                    String editorial = comic.getEditorial().toLowerCase();
                    String serie = comic.getSerieName().toLowerCase();
                    String query1 = query.toLowerCase();
                    if(displayName.contains(query1) || serie.contains(query1) || editorial.contains(query1)){
                        comicsFiltered.add(comic);
                    }
                    }

                    if(comicsFiltered.size() == 0 && noComicsTX.getVisibility() != View.VISIBLE){

                        noResultsTX.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                recyclerView.setVisibility(View.VISIBLE);
                adapter.addAll(comicsFiltered);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    prepareComics();

                }
                return false;
            }
        });


    }


    @OnClick(R.id.addComic)
    public void addComic(){

        AddComicFragment addFrag= new AddComicFragment();
        openFragment(addFrag,addFrag.PLACE_IN_DRAWER,"add");

    }


}
