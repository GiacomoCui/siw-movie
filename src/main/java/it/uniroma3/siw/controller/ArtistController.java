package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArtistController {
	@Autowired
	private ArtistRepository artistRepository;



	@GetMapping("/artists")
	public String showArtist(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "artists.html";
	}
	@GetMapping("/admin/indexArtist")
	public String indexArtist(){return "admin/indexArtist.html";}

	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}

	@PostMapping("/admin/artists")
	public String newArtist(@ModelAttribute("artist") Artist artist, Model model) {
		if (!artistRepository.existsByNomeAndCognome(artist.getNome(), artist.getCognome())) {
			this.artistRepository.save(artist);
			model.addAttribute("artist", artist);
			return "admin/indexartist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html";
		}
	}
}
