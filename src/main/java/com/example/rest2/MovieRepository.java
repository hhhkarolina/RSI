package com.example.rest2;

import java.util.List;

public interface MovieRepository {
    List<Movie> getAllMovies();
    Movie getMovie(int id) throws MovieNotFoundEx;
    Movie updateMovie(int id, String name, int releaseYear, double rating, String genre) throws MovieNotFoundEx;
    boolean deleteMovie(int id) throws MovieNotFoundEx;
    Movie addMovie(Movie movie) throws MovieExistsEx;
    Movie watchMovie(int id) throws MovieNotFoundEx;
    List<Movie> getUnwatchedMovies() throws MovieNotFoundEx;

}
