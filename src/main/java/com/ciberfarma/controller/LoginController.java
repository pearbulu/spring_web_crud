package com.ciberfarma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ciberfarma.dto.AutentacionFilter;
import com.ciberfarma.service.AutenticacionService;
import com.ciberfarma.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
	private final AutenticacionService autenticacionService;

	@GetMapping("iniciar-sesion")
	public String iniciarSesion(
			@ModelAttribute AutentacionFilter filter, 
			Model model, 
			RedirectAttributes flash,
			HttpSession session
	) {

		var usuario = autenticacionService.autenticathe(filter);

		if (usuario == null) {
			var mensaje = Alert.sweetAlertError("Cuenta y/o clave inválido");
			model.addAttribute("alert", mensaje);
			model.addAttribute("filter", filter);
			return "login";
		}
		
		if (!usuario.getActivo()) {
			var mensaje = Alert.sweetAlertError("La cuenta se encuentra inactiva");
			model.addAttribute("alert", mensaje);
			model.addAttribute("filter", filter);
			return "login";
		}
		
		//Guardamos los datos que sean necesarios en sesión
		session.setAttribute("idUsuario", usuario.getIdUsuario());
		session.setAttribute("fullName", usuario.getFullName());
		session.setAttribute("idTipo", usuario.getTipo().getIdTipo());
		
		// Tiempo máximo de inactividad: 5 minutos
		session.setMaxInactiveInterval(300);

		String alert = Alert.sweetImageUrl("Bienvenido a Ciberfarma", usuario.getFullName(), "/imagenes/mapache_pedro.gif");
		flash.addFlashAttribute("alert", alert);
		return "redirect:/dashboard";
	}
	
	@GetMapping("cerrar-sesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
