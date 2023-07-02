package it.uniroma3.siw.controller.validator;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MovieValidator implements Validator {
	@Autowired
	MovieRepository movieRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return Movie.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "titolo", "NotBlank.movie.titolo");
		ValidationUtils.rejectIfEmpty(errors, "anno", "NotBlank.movie.anno");
		Movie movie = (Movie) target;
		if (movieRepository.existsByTitolo(movie.getTitolo())) {
			errors.reject("movie.duplicati");
		}
	}
}