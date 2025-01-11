package unam.dgtic.spv.core.controller;

import unam.dgtic.spv.core.dto.UsuarioDTO;
import unam.dgtic.spv.core.service.Usuario.UsuarioService;
import unam.dgtic.spv.security.jwt.JWTTokenProvider;
import unam.dgtic.spv.security.request.JwtRequest;
import unam.dgtic.spv.security.request.LoginUserRequest;
import unam.dgtic.spv.security.service.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
public class HomeController {

	private final UsuarioService usuarioService;
	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
	private final UserDetailsServiceImpl userDetailsService;

	// Controller Injection
	public HomeController( UsuarioService usuarioService,
						  AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {

		this.usuarioService = usuarioService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}
	@GetMapping("/")
	public String home(Model model) {
		return "index";
	}
	@GetMapping("/index")
	public String index() {
		return "redirect:/";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login_success_handler")
	public String loginSuccessHandler() {
		System.out.println("Logging user login success...");
		return "index";
	}
	@PostMapping("/login_failure_handler")
	public String loginFailureHandler() {
		System.out.println("Login failure handler....");
		return "login";
	}
	@PostMapping("/token")
	public String createAuthenticationToken(Model model, HttpSession session,
											@ModelAttribute LoginUserRequest loginUserRequest, HttpServletResponse res) throws Exception {
		log.info("LoginUserRequest {}", loginUserRequest);
		try {
			UsuarioDTO user = usuarioService.findByUseEmail(loginUserRequest.getUsername());
			if (user.getActivo() == true) {
				Authentication authentication = authenticate(loginUserRequest.getUsername(),
						loginUserRequest.getPassword());
				log.info("authentication {}", authentication);
				String jwtToken = jwtTokenProvider.generateJwtToken(authentication, user);
				log.info("jwtToken {}", jwtToken);
				JwtRequest jwtRequest = new JwtRequest(jwtToken, user.getId(), user.getEmail(),
						jwtTokenProvider.getExpiryDuration(), authentication.getAuthorities());
				log.info("jwtRequest {}", jwtRequest);
				Cookie cookie = new Cookie("token",jwtToken);
				cookie.setMaxAge(Integer.MAX_VALUE);
				res.addCookie(cookie);
				session.setAttribute("msg","Login OK!");
			}
		} catch (UsernameNotFoundException | BadCredentialsException e) {
			session.setAttribute("msg","Bad Credentials");
			return "redirect:/login";
		}
		return "redirect:/index";
	}

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
