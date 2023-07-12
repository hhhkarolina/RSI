package com.example.rest2;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.rest2.MovieStatus.NOT_WATCHED;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
public class MovieController {
    private final MovieRepository dataRepo = new MovieRepositoryImpl();
    ProducerExchange producerExchange = new ProducerExchange();
    String STANDARD_LOG = "standard";
    String WATCH_LOG = "watch";

    @GetMapping("/movies/{id}")
    public ResponseEntity<EntityModel<Movie>> getMovie(@PathVariable int id) {
        try {
            System.out.println("...called GET");
            Movie movie = dataRepo.getMovie(id);

            producerExchange.publishMessage(STANDARD_LOG, "get movie" + id);

            return ResponseEntity
                            .ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(EntityModel.of(movie,
                                                    linkTo(methodOn(MovieController.class).getMovie(id)).withSelfRel(),
                                                    linkTo(methodOn(MovieController.class).deleteMovie(id)).withRel("delete"),
                                                    linkTo(methodOn(MovieController.class).getAllMovies()).withRel("getAll")
                                            ));

        } catch (MovieNotFoundEx e) {
            System.out.println("...GET exception");
            throw(e);
        }
    }

    @GetMapping("/movies/")
    public CollectionModel<EntityModel<Movie>> getAllMovies() {
        try {
            System.out.println("...called GET");

            List<EntityModel<Movie>> movieModels = new ArrayList<>();
            for(Movie movie : dataRepo.getAllMovies()) {
                addLinks(movieModels, movie);
            }

            producerExchange.publishMessage(STANDARD_LOG, "get all movies");

            return CollectionModel.of(movieModels,
                    linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());


        } catch (MovieNotFoundEx e ) {
            System.out.println("...GET exception");
            throw(e);
        }
    }

    private void addLinks(List<EntityModel<Movie>> movieModels, Movie movie) {
        EntityModel<Movie> movieModel = EntityModel.of(movie);

        movieModel.add(linkTo(methodOn(MovieController.class).getMovie(movie.getId())).withSelfRel());
        movieModel.add(linkTo(methodOn(MovieController.class).deleteMovie(movie.getId())).withRel("delete"));
        movieModel.add(linkTo(methodOn(MovieController.class).getAllMovies()).withRel("getAll"));

        if (movie.getStatus() == NOT_WATCHED) {
            movieModel.add(linkTo(methodOn(MovieController.class).watchMovie(movie.getId())).withRel("watch"));
        }

        movieModels.add(movieModel);
    }

    @PostMapping("/movies/")
    public ResponseEntity<EntityModel<Movie>> addMovie(@RequestBody Movie movie) {
        try {
            System.out.println("...called POST");
            Movie savedMovie = dataRepo.addMovie(movie);

            EntityModel<Movie> movieModel = EntityModel.of(savedMovie,
                    linkTo(methodOn(MovieController.class).getMovie(savedMovie.getId())).withSelfRel());

            producerExchange.publishMessage(STANDARD_LOG, "add movie" + movie);

            return ResponseEntity.status(HttpStatus.CREATED).body(movieModel);

        } catch (MovieExistsEx e) {
            System.out.println("...POST exception");
            throw(e);
        }
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<EntityModel<?>> deleteMovie(@PathVariable int id) {
        try{
            System.out.println("...called DELETE");
            dataRepo.deleteMovie(id);

            producerExchange.publishMessage(STANDARD_LOG, "delete movie" + id);

            return ResponseEntity.noContent().build();
        } catch (MovieNotFoundEx e){
            System.out.println("...DELETE exception");
            throw(e);
        }

    }

    @PutMapping("/movies/{id}")
    public Movie updatePerson(@RequestBody Movie movie) {
        try{
            System.out.println("...called PUT");
            producerExchange.publishMessage(STANDARD_LOG, "update movie" + movie);
            return dataRepo.updateMovie(movie.getId(), movie.getName(), movie.getReleaseYear(), movie.getRating(), movie.getGenre());
        } catch (MovieNotFoundEx e){
            System.out.println("...PUT exception");
            throw(e);
        }

    }

    @GetMapping("/movies/unwatched")
    public CollectionModel<EntityModel<Movie>> getUnwatchedMovies() {
        try{
           System.out.println("...called GET");

            List<EntityModel<Movie>> movieModels = new ArrayList<>();
            for(Movie movie : dataRepo.getUnwatchedMovies()) {
                addLinks(movieModels, movie);
            }

            producerExchange.publishMessage(WATCH_LOG, "get unwached movies");

            return CollectionModel.of(movieModels,
                    linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());

        } catch (MovieNotFoundEx e){
            System.out.println("...GET exception");
            throw(e);
        }
    }

    @PatchMapping("/movies/{id}/watch")
    public ResponseEntity<EntityModel<Movie>> watchMovie(@PathVariable int id) {
        try{
            System.out.println("...called PATCH");
            Movie watchedMovie = dataRepo.watchMovie(id);

            EntityModel<Movie> movieModel = EntityModel.of(watchedMovie,
                    linkTo(methodOn(MovieController.class).getMovie(watchedMovie.getId())).withSelfRel());


            producerExchange.publishMessage(WATCH_LOG, "watch" + id);
            return ResponseEntity.ok().body(movieModel);

        } catch (MovieNotFoundEx e){
            System.out.println("...PATCH exception");
            throw(e);
        }
    }
}


