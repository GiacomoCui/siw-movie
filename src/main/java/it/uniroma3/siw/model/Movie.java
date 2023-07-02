package it.uniroma3.siw.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@AllArgsConstructor
@Data
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String titolo;

	@NotNull
	@Min(1900)
	@Max(2023)
	private Integer anno;
	private String image;

	@OneToMany
	@JoinColumn(name = "movie_id")
	private List<News> notizie;

	@ManyToMany
	private List<Artist> attori;

	@ManyToOne
	private Artist regista;

	public Movie(){
		attori = new ArrayList<>();
		notizie = new ArrayList<>();
	}

	@Transient
	public String getImagePath(){
		if(image == null || id == null) return null;
		return "/foto-movie/" + id + "/" + image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Movie)) return false;
		Movie movie = (Movie) o;
		return Objects.equals(titolo, movie.titolo) && Objects.equals(anno, movie.anno);
	}

	@Override
	public int hashCode() {
		return Objects.hash(titolo, anno);
	}
}