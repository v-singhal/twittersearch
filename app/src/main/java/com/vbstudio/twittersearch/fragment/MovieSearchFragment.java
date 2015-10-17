package com.vbstudio.twittersearch.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vbstudio.twittersearch.Constants.ConstantKeys;
import com.vbstudio.twittersearch.R;
import com.vbstudio.twittersearch.dom.MovieResponse.MovieBasicData;
import com.vbstudio.twittersearch.dom.MovieResponse.MovieBasicResponseImageData;
import com.vbstudio.twittersearch.dom.MovieResponse.MovieResponseBasicData;
import com.vbstudio.twittersearch.fragment.adapter.MovieSuggestionsRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.vbstudio.twittersearch.utils.KeyboardUtils.hideKeyboard;
import static com.vbstudio.twittersearch.utils.StringUtils.isValidString;

/**
 * Created by vaibhav on 23/8/15.
 */
public class MovieSearchFragment extends BaseFragment implements TextWatcher, TextView.OnEditorActionListener {

    private static final int MAX_MOVIE_SUGGESTIONS = 5;

    private RecyclerView movieSuggestionRecyclerView;
    private LinearLayoutManager movieSuggestionRecyclerLayoutManager;
    private RecyclerView.Adapter movieSuggestionAdapter;

    MovieSearchTask movieSearchTask;
    AutoCompleteTextView txtAutoCompleteMovieSearch;

    public static MovieSearchFragment newInstance() {
        MovieSearchFragment fragment = new MovieSearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_movie_search, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView(view);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        final int id = view.getId();

        if (id == R.id.btnClearMovieSearch) {
            cancelMovieSearchTask();
            ((EditText) getView().findViewById(R.id.txtAutoCompleteMovieSearch)).setText("");
            invalidateSuggestions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        cancelMovieSearchTask();
        movieSearchTask = new MovieSearchTask();
        movieSearchTask.execute(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard(getActivity());
            /*****************************
             Implement on search click
             /*****************************/
            return true;
        }
        return false;
    }

    @Override
    public void onErrorResponse(Request request, VolleyError error) {
        super.onErrorResponse(request, error);
    }

    @Override
    public void onResponse(Request<JSONObject> request, JSONObject response) {
        super.onResponse(request, response);

        if (response != null) {
            if (response.optInt("responseCode") == 1000) {

            }
        }
    }

    @Override
    public JSONObject processServerGETResponse(Request<String> request, String response) {
        return super.processServerGETResponse(request, response);
    }

    private void setupView(View view) {
        movieSearchTask = new MovieSearchTask();

        txtAutoCompleteMovieSearch = (AutoCompleteTextView) view.findViewById(R.id.txtAutoCompleteMovieSearch);

        txtAutoCompleteMovieSearch.addTextChangedListener(this);
        txtAutoCompleteMovieSearch.setThreshold(1);
        txtAutoCompleteMovieSearch.setOnEditorActionListener(this);

        movieSuggestionRecyclerView = (RecyclerView) view.findViewById(R.id.movieSuggestionRecyclerView);
        movieSuggestionRecyclerLayoutManager = new LinearLayoutManager(getActivity());
        movieSuggestionRecyclerView.setLayoutManager(movieSuggestionRecyclerLayoutManager);

        view.findViewById(R.id.btnClearMovieSearch).setOnClickListener(this);
    }

    private void searchForMovie() {
        String searchQuery = "";
        String firstCharInSearch = "";

        if (isValidString(txtAutoCompleteMovieSearch.getText().toString())) {
            showLoadingIndicator();
            searchQuery = txtAutoCompleteMovieSearch.getText().toString();
            firstCharInSearch = searchQuery.substring(0, 1);

            String strGetMovieAutoSuggest = "http://sg.media-imdb.com/suggests"
                    + "/"
                    + firstCharInSearch
                    + "/"
                    + searchQuery
                    + ".json";

            Log.i(BaseFragment.LOG_TAG, "SEARCH FIRST CHAR: " + firstCharInSearch);
            Log.i(BaseFragment.LOG_TAG, "SEARCH SEARCH QUERY: " + searchQuery);
            Log.i(BaseFragment.LOG_TAG, "SEARCH URL: " + strGetMovieAutoSuggest);

            setNetworkManager(strGetMovieAutoSuggest).jsonGetRequest(ConstantKeys.MOVIE_AUTO_SUGGEST, strGetMovieAutoSuggest, new Response.Listener<String>() {
                @Override
                public void onResponse(Request<String> request, String response) {
                    processSuggestions(processServerGETResponse(request, response));
                }
            }, this, false);
        } else {
            invalidateSuggestions();
        }
    }

    private void processSuggestions(JSONObject response) {
        JSONArray movieSuggestionArrayFromServer = response.optJSONArray(ConstantKeys.MOVIE_RESPONSE_BASIC_ARRAY);
        MovieResponseBasicData movieResponseBasicData = new MovieResponseBasicData();
        List<MovieBasicData> movieSuggestionDataList = new ArrayList<>();
        int resultCount = Math.min(MAX_MOVIE_SUGGESTIONS, movieSuggestionArrayFromServer.length());

        movieResponseBasicData.setMovieResponseV(response.optString(ConstantKeys.MOVIE_RESPONSE_V));
        movieResponseBasicData.setMovieResponseQuery(response.optString(ConstantKeys.MOVIE_RESPONSE_QUERY));

        for (int counter = 0; counter < resultCount; counter++) {
            JSONObject movieSuggestionFromServer = movieSuggestionArrayFromServer.optJSONObject(counter);
            MovieBasicData movieSuggestionData = new MovieBasicData();

            movieSuggestionData.setMovieResponseBasicId(movieSuggestionFromServer.optString(ConstantKeys.MOVIE_RESPONSE_BASIC_ID));
            movieSuggestionData.setMovieResponseBasicName(movieSuggestionFromServer.optString(ConstantKeys.MOVIE_RESPONSE_BASIC_NAME));
            movieSuggestionData.setMovieResponseBasicCast(movieSuggestionFromServer.optString(ConstantKeys.MOVIE_RESPONSE_BASIC_CAST));
            movieSuggestionData.setMovieResponseBasicType(movieSuggestionFromServer.optString(ConstantKeys.MOVIE_RESPONSE_BASIC_TYPE));
            movieSuggestionData.setMovieResponseBasicYear(movieSuggestionFromServer.optString(ConstantKeys.MOVIE_RESPONSE_BASIC_YEAR));
            movieSuggestionData.setMovieBasicResponseImageData(MovieBasicResponseImageData.convertResponseToObject(movieSuggestionFromServer));

            movieSuggestionDataList.add(movieSuggestionData);
        }
        movieResponseBasicData.setMovieResponseBasicDataList(movieSuggestionDataList);

        displaySuggestions(movieResponseBasicData);
    }

    private void displaySuggestions(MovieResponseBasicData movieResponseBasicData) {
        movieSuggestionAdapter = new MovieSuggestionsRecyclerAdapter(movieSuggestionRecyclerView, movieResponseBasicData.getMovieResponseBasicDataList(), getActivity(), this);
        movieSuggestionRecyclerView.setAdapter(movieSuggestionAdapter);
    }

    private void invalidateSuggestions() {
        movieSuggestionAdapter = new MovieSuggestionsRecyclerAdapter(movieSuggestionRecyclerView, new ArrayList<MovieBasicData>(), getActivity(), this);
        movieSuggestionRecyclerView.setAdapter(movieSuggestionAdapter);
    }


    private void cancelMovieSearchTask() {
        cancelAsyncTask(movieSearchTask);
        getNetworkManager().cancel();
    }

    //*************************************************************************/

    private class MovieSearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... place) {
            String data = "";
            if (!isCancelled() && getActivity() != null) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            searchForMovie();
        }
    }
}