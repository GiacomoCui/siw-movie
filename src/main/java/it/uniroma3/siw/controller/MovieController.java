package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MovieController {
	@Autowired
	private MovieValidator movieValidator;
	@Autowired
	private MovieService movieService;
	@Autowired
	private ArtistService artistService;

	@GetMapping("/index")
	public String index() {
		return "index.html";
	}

	@GetMapping("/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@PostMapping("/admin/newMovie")
	public String newMovie(@Valid @RequestParam("files") MultipartFile file, @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) throws IOException {
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieService.saveNewMovie(file, movie);
			model.addAttribute("movie", movie);
			model.addAttribute("movies", movieService.getMovies());
			return "movies.html";
		} else {
			System.out.println(bindingResult.getAllErrors().get(0));
			return "admin/formNewMovie.html";
		}
	}

	@GetMapping("/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", movieService.getMovies());
		return "movies.html";
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer anno) {
		model.addAttribute("movies", movieService.searchMovies(anno));
		return "foundMovies.html";
	}

	@GetMapping("/admin/manageMovies")
	public String manageMovie(Model model) {
		model.addAttribute("movies", movieService.getMovies());
		return "admin/manageMovies.html";
	}

	@GetMapping("/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = movieService.getMovie(id);
		model.addAttribute("movie", movie);

		if (movie.getRegista() != null) {
			model.addAttribute("director", movie.getRegista());
		}

		if (movie.getAttori() != null) {
			model.addAttribute("actors", movie.getAttori());
		}

		return "admin/formUpdateMovie.html";
	}

	@GetMapping("/admin/addDirectorToMovie/{id}")
	public String addDirectorToMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistService.getArtists());
		model.addAttribute("movie", movieService.getMovie(id));
		return "admin/directorsToAdd.html";
	}

	@Transactional
	@GetMapping("/admin/setDirectorToMovie/{idArtist}/{idMovie}")
	public String setDirectorMovie(@PathVariable("idArtist") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieService.setDirectorToMovie(idA, idM);
		model.addAttribute("movie", movie);
		model.addAttribute("artist", movie.getRegista());
		model.addAttribute("movies", movieService.getMovies());
		return "admin/manageMovies";
	}

	@GetMapping("/admin/editActor/{id}")
	public String editActor(@PathVariable("id") Long id, Model model) {
		Movie movie = movieService.getMovie(id);

		List<Artist> notMovieActors = artistService.notMovieActors(movie);
		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", notMovieActors);

		return "admin/actorsToAdd.html";
	}

	@Transactional
	@GetMapping("/admin/addActorToMovie/{idActor}/{idMovie}")
	public String addActorToMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieService.addActorToMovie(idA, idM);
		List<Artist> notMovieActors = artistService.notMovieActors(movie);

		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", notMovieActors);

		return "admin/actorsToAdd.html";
	}

	@Transactional
	@GetMapping("/admin/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActorFromMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieService.removeActorFromMovie(idA, idM);
		List<Artist> notMovieActors = artistService.notMovieActors(movie);

		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", notMovieActors);

		return "admin/actorsToAdd.html";
	}


}