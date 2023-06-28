package it.uniroma3.siw.model;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String cognome;
	private LocalDate dataNascita;

	@ManyToMany(mappedBy = "attori")
	private List<Movie> partecipazione_film;

	@OneToMany(mappedBy = "regista")
	private List<Movie> film_diretti;

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
}
