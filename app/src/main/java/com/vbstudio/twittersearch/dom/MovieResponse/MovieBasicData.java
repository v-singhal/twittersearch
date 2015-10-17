package com.vbstudio.twittersearch.dom.MovieResponse;

/**
 * Created by vaibhav on 18/10/15.
 */
public class MovieBasicData {

    private String movieResponseBasicId;
    private String movieResponseBasicName;
    private String movieResponseBasicCast;
    private String movieResponseBasicType;
    private String movieResponseBasicYear;
    private MovieBasicResponseImageData movieBasicResponseImageData;

    public String getMovieResponseBasicId() {
        return movieResponseBasicId;
    }

    public void setMovieResponseBasicId(String movieResponseBasicId) {
        this.movieResponseBasicId = movieResponseBasicId;
    }

    public String getMovieResponseBasicName() {
        return movieResponseBasicName;
    }

    public void setMovieResponseBasicName(String movieResponseBasicName) {
        this.movieResponseBasicName = movieResponseBasicName;
    }

    public String getMovieResponseBasicCast() {
        return movieResponseBasicCast;
    }

    public void setMovieResponseBasicCast(String movieResponseBasicCast) {
        this.movieResponseBasicCast = movieResponseBasicCast;
    }

    public String getMovieResponseBasicType() {
        return movieResponseBasicType;
    }

    public void setMovieResponseBasicType(String movieResponseBasicType) {
        this.movieResponseBasicType = movieResponseBasicType;
    }

    public String getMovieResponseBasicYear() {
        return movieResponseBasicYear;
    }

    public void setMovieResponseBasicYear(String movieResponseBasicYear) {
        this.movieResponseBasicYear = movieResponseBasicYear;
    }

    public MovieBasicResponseImageData getMovieBasicResponseImageData() {
        return movieBasicResponseImageData;
    }

    public void setMovieBasicResponseImageData(MovieBasicResponseImageData movieBasicResponseImageData) {
        this.movieBasicResponseImageData = movieBasicResponseImageData;
    }
}
