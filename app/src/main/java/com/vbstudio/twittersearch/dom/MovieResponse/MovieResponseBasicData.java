package com.vbstudio.twittersearch.dom.MovieResponse;

import java.util.List;

/**
 * Created by vaibhav on 18/10/15.
 */
public class MovieResponseBasicData {
    
    private String movieResponseV;
    private String movieResponseQuery;
    private List<MovieBasicData> movieResponseBasicDataList;

    public String getMovieResponseV() {
        return movieResponseV;
    }

    public void setMovieResponseV(String movieResponseV) {
        this.movieResponseV = movieResponseV;
    }

    public String getMovieResponseQuery() {
        return movieResponseQuery;
    }

    public void setMovieResponseQuery(String movieResponseQuery) {
        this.movieResponseQuery = movieResponseQuery;
    }

    public List<MovieBasicData> getMovieResponseBasicDataList() {
        return movieResponseBasicDataList;
    }

    public void setMovieResponseBasicDataList(List<MovieBasicData> movieResponseBasicDataList) {
        this.movieResponseBasicDataList = movieResponseBasicDataList;
    }
}
