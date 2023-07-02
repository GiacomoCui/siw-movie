package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthenticationController {
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private UserService userService;

	@GetMapping("admin/indexAdmin.html")
	public String adminIndex(Model model){
		return "admin/indexAdmin.html";
	}
	@GetMapping("admin/index.html")
	public String defaultIndex(Model model){
		return "index.html";
	}

	@GetMapping(value = "/register")
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "formLogin.html";
		}
		else {
			UserDetails userDetails = userService.getUserDetails();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRuolo().equals(Credentials.RUOLO_ADMIN)) {
				return adminIndex(model);
			}
		}
		return "formLogin.html";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = userService.getUserDetails();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credentials.RUOLO_ADMIN)) {
			return adminIndex(model);
		}
		return defaultIndex(model);
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
							   BindingResult userBindingResult, @Valid
							   @ModelAttribute("credentials") Credentials credentials,
							   BindingResult credentialsBindingResult,
							   Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			userService.setUser(credentials, user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful";
		}
		return "registerUser";
	}
}