package it.uniroma3.siw.model;

import javax.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

	public static final String RUOLO_DEFAULT = "DEFAULT";
	public static final String RUOLO_ADMIN = "ADMIN";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;
	private String password;
	private String ruolo;

	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	@OneToMany
	private List<News> recensioni;
}
