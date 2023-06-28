package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
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
	private MovieRepository movieRepository;
	@Autowired
	private MovieValidator movieValidator;
	@Autowired
	private ArtistRepository artistRepository;
	@Autowired
	private MovieService movieService;

	@GetMapping("/index.html")
	public String index() {
		return "index.html";
	}

	@GetMapping("/admin/indexMovie")
	public String indexMovie(){return "admin/indexMovie.html";}

	@GetMapping("/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@PostMapping("/movie")
	public String newMovie(@Valid @RequestParam("files") MultipartFile file, @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) throws IOException {
		this.movieValidator.validate(movie, bindingResult);
		if(!bindingResult.hasErrors()){
			this.movieService.saveNewMovie(movie, file);
			model.addAttribute("movie", movie);
			return "movie.html";
		}
		else {
			//model.addAttribute("messaggioErrore", bindingResult.getAllErrors());
			return "admin/formNewMovie.html";
		}
	}

	@GetMapping("/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieRepository.findById(id).get());
		return "movie.html";
	}

	@GetMapping("/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieRepository.findAll());
		return "movies.html";
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer anno) {
		model.addAttribute("movies", this.movieRepository.findByAnno(anno));
		return "foundMovies.html";
	}

	@GetMapping("/admin/manageMovie")
	public String manageMovie(Model model) {
		model.addAttribute("movies", this.movieRepository.findAll());
		return "admin/manageMovie.html";
	}

	@GetMapping("/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieRepository.findById(id).get();
		model.addAttribute("movie", movie);

		if(movie.getRegista() != null) {
			model.addAttribute("director", movie.getRegista());
		}

		if(movie.getAttori() != null) {
			model.addAttribute("actors", movie.getAttori());
		}

		return "admin/formUpdateMovie.html";
	}

	@GetMapping("/admin/addDirectorToMovie/{id}")
	public String addDirectorToMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		model.addAttribute("movie", this.movieRepository.findById(id).get());
		return "admin/addDirectorToMovie.html";
	}

	@Transactional
	@GetMapping("/admin/setDirectorToMovie/{idArtist}/{idMovie}")
	public String setDirectorMovie(@PathVariable("idArtist") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieService.setDirectorToMovie(idA, idM);
		model.addAttribute("movie", movie);
		model.addAttribute("artist", movie.getRegista());
		return "admin/formUpdateMovie.html";
	}

	@GetMapping("/admin/editActor/{id}")
	public String editActor(@PathVariable("id") Long id, Model model) {
		Movie movie = movieRepository.findById(id).get();

		List<Artist> notMovieActors = (List<Artist>) artistRepository.findAll();
		notMovieActors.removeAll(movie.getAttori());

		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", notMovieActors);

		return "admin/addActorsToMovie.html";
	}

	@Transactional
	@GetMapping("/admin/addActorToMovie/{idActor}/{idMovie}")
	public String addActorToMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieRepository.findById(idM).get();
		Artist actor = artistRepository.findById(idA).get();

		movie.getAttori().add(actor);
		actor.getPartecipazione_film().add(movie);

		artistRepository.save(actor);
		movieRepository.save(movie);

		List<Artist> notMovieActors = (List<Artist>) artistRepository.findAll();
		List<Artist> newList = new ArrayList<Artist>(notMovieActors);
		newList.removeAll(movie.getAttori());

		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", newList);

		return "admin/addActorsToMovie.html";
	}

	@Transactional
	@GetMapping("/admin/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActorFromMovie(@PathVariable("idActor") Long idA, @PathVariable("idMovie") Long idM, Model model) {
		Movie movie = movieRepository.findById(idM).get();
		Artist actor = artistRepository.findById(idA).get();

		artistRepository.save(actor);
		movieRepository.save(movie);

		movie.getAttori().remove(actor);
		actor.getPartecipazione_film().remove(movie);

		List<Artist> notMovieActors = (List<Artist>) artistRepository.findAll();
		List<Artist> newList = new ArrayList<Artist>(notMovieActors);
		newList.removeAll(movie.getAttori());

		model.addAttribute("movie", movie);
		model.addAttribute("movieActors", movie.getAttori());
		model.addAttribute("notMovieActors", newList);

		return "admin/addActorsToMovie.html";
	}
}
