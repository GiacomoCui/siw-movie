package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
	public List<Movie> findByAnno(Integer anno);
	public boolean existsByTitoloAndAnno(String titolo, Integer anno);
	public List<Movie> findAll();
}
