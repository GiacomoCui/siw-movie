package it.uniroma3.siw.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	private String cognome;
	private String dataNascita;
	private String dataMorte;

	private String image;

	@ManyToMany(mappedBy = "attori")
	private List<Movie> partecipazione_film;

	@OneToMany(mappedBy = "regista")
	private List<Movie> film_diretti;

	public Artist(){
		partecipazione_film = new ArrayList<>();
		film_diretti = new ArrayList<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Artist)) return false;
		Artist artist = (Artist) o;
		return Objects.equals(nome, artist.nome) && Objects.equals(cognome, artist.cognome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, cognome);
	}

	@Transient
	public String getImagePath(){
		if(image == null || id == null) return null;
		return "/foto-movie/" + id + "/" + image;
	}

}

