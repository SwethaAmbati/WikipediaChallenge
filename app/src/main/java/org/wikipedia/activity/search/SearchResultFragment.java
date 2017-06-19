package org.wikipedia.activity.search;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.wikipedia.ListAdapter;
import org.wikipedia.R;
import org.wikipedia.model.WikiData;
import org.wikipedia.rest.ApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment implements  SearchView.OnQueryTextListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "SearchResultFragment";
    RecyclerView recyclerView;
    SearchView searchView;
    private ListAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager mSGLM;
    private GridLayoutManager mGLM;
    LinearLayout lLayout;
    private View fragmentView;
    private List<WikiData> wikiDataList ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String QUERY = "query";
    private static final String PAGEIMAGES = "pageimages";
    private static final String JSON = "json";
    private static final String THUMBNAIL = "thumbnail";
    private static final int PITHUMBSIZE = 180;
    private static final int PILIMIT = 50;
    private static final String PREFIXSEARCH = "prefixsearch";
    private static final int GPSLIMIT = 50;

    private static final String PAGES = "pages";
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
    private static final String SOURCE = "source";

    private OnFragmentInteractionListener mListener;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_search_result, container, false);
        fragmentView.setTag(TAG);
        wikiDataList = new ArrayList<>();
        lLayout = (LinearLayout) fragmentView.findViewById(R.id.wiki_llayout) ;
        lLayout.setVisibility(View.VISIBLE);
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getWikiImages(String newText) {
        final String GPSSEARCH = newText;
        Log.v("BUILT URL",ApiClient.getApi().getWikiResponse(QUERY,PAGEIMAGES,JSON,THUMBNAIL,PITHUMBSIZE,PILIMIT,PREFIXSEARCH,GPSSEARCH,GPSLIMIT).request().url().toString());
        ApiClient.getApi().getWikiResponse(QUERY,PAGEIMAGES,JSON,THUMBNAIL,PITHUMBSIZE,PILIMIT,PREFIXSEARCH,GPSSEARCH,GPSLIMIT).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject object = response.body().getAsJsonObject();
                if(GPSSEARCH.length()>0){

                    try {
                    JSONObject jObject = new JSONObject(object.toString());
                    JSONObject jsonQuery = jObject.getJSONObject(QUERY);
                    JSONObject jsonPages = jsonQuery.getJSONObject(PAGES);

                    Iterator<?> keys = jsonPages.keys();
                    String img_url = "";
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        if (jsonPages.get(key) instanceof JSONObject) {
                            Object keyvalue = jsonPages.get(key);
                            WikiData data = new WikiData();
                            if (keyvalue instanceof JSONObject) {
                                String title = ((JSONObject) keyvalue).getString(TITLE);
                                data.title = title;
                                if (((JSONObject) keyvalue).has(THUMBNAIL)) {
                                    Object img = ((JSONObject) keyvalue).get(THUMBNAIL);
                                    img_url = ((JSONObject) img).getString(SOURCE);
                                    data.imgThumbnailUrl = img_url;
                                }

                                wikiDataList.add(data);


                            }
                        }
                    }

                    // Uncomment for grid layout
                    /* lLayout.setVisibility(View.GONE);
                    mGLM = new GridLayoutManager(MainActivity.this, 2);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(mGLM);
                    mAdapter = new ListAdapter(MainActivity.this,wikiDataList);
                    recyclerView.setAdapter(mAdapter);*/


                    // Staggered grid layout

                    lLayout.setVisibility(View.GONE);
                    mSGLM = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    mSGLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                    recyclerView = (RecyclerView) fragmentView.findViewById(R.id.list);
                    recyclerView.setLayoutManager(mSGLM);
                    mAdapter = new ListAdapter(getActivity(), wikiDataList);
                    recyclerView.setAdapter(mAdapter);


                    } catch (JSONException e) {
                    e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getWikiImages(newText);
        wikiDataList.clear();

        return false;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                return true;
            }
        });

    }


}
