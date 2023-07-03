package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@Controller
public class ArtistController {
	@Autowired
	private ArtistValidator artistValidator;
	@Autowired
	private ArtistService artistService;


	@GetMapping("/artists")
	public String showArtists(Model model) {
		model.addAttribute("artists", artistService.getArtists());
		return "artists.html";
	}

	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist";
	}

	@PostMapping("/admin/artists")
	public String newArtist(@Valid @RequestParam("dataNascita")String dataN, @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model){
		artistValidator.validate(artist, bindingResult);
		if(!bindingResult.hasErrors()){
			artistService.saveNewArtist(artist, dataN);
			model.addAttribute("artist", artist);
			return "index.html";
		} else {
			return "admin/formNewArtist.html";
		}
	}

}