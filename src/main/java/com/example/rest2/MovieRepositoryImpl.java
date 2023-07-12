package com.example.rest2;

import java.util.ArrayList;
import java.util.List;


public class MovieRepositoryImpl implements MovieRepository{
    private final List<Movie> movieList;

    public MovieRepositoryImpl() {
        movieList = new ArrayList<>();
        movieList.add(new Movie(1, "Pulp Fiction", 1999, 8.2, "Drama"));
        movieList.add(new Movie(2, "Film", 2020, 5.0, "Drama"));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieList;
    }

    @Override
    public Movie getMovie(int id) throws MovieNotFoundEx{
        for(Movie movie : movieList) {
            if(movie.getId() == id) {
                return movie;
            }
        }
        throw new MovieNotFoundEx();
    }

    @Override
    public Movie updateMovie(int id, String name, int releaseYear, double rating, String genre) throws MovieNotFoundEx{
        for(Movie movie : movieList) {
            if(movie.getId() == id) {
                movie.setName(name);
                movie.setGenre(genre);
                movie.setRating(rating);
                movie.setReleaseYear(releaseYear);
                return movie;
            }
        }
        throw new MovieNotFoundEx();
    }

    @Override
    public boolean deleteMovie(int id) throws MovieNotFoundEx{
        for (Movie movie : movieList) {
            if (movie.getId() == id) {
                movieList.remove(movie);
                return true;
            }
        }
        throw new MovieNotFoundEx();
    }

    @Override
    public Movie addMovie(Movie movie) throws MovieExistsEx{
        for (Movie theMovie : movieList) {
            if (theMovie.getId() == movie.getId()) {
                throw new MovieExistsEx();
            }
        }
        movieList.add(movie);
        return movie;
    }

    @Override
    public List<Movie> getUnwatchedMovies() throws MovieNotFoundEx {
        List<Movie> unwatchedMovies = new ArrayList<>();
        for(Movie movie: movieList) {
            if(movie.getStatus() == MovieStatus.NOT_WATCHED) {
                unwatchedMovies.add(movie);
            }
        }

        if(unwatchedMovies.size() == 0) {
            throw new MovieNotFoundEx();
        } else {
            return unwatchedMovies;
        }
    }

    @Override
    public Movie watchMovie(int id) throws MovieNotFoundEx {
        for(Movie movie: movieList) {
            if(movie.getId() == id) {
                movie.setStatus(MovieStatus.WATCHED);
                return movie;
            }
        }
        throw new MovieNotFoundEx();
    }
}
