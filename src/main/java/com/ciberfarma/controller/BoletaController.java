package com.ciberfarma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ciberfarma.dto.ProductoSeleccionado;
import com.ciberfarma.model.Boleta;
import com.ciberfarma.model.DetalleBoleta;
import com.ciberfarma.service.BoletaService;
import com.ciberfarma.service.ProductoService;
import com.ciberfarma.service.UsuarioService;
import com.ciberfarma.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("boleta")
@RequiredArgsConstructor
@SessionAttributes("seleccionados")
public class BoletaController {

	private final BoletaService boletaService;
	private final ProductoService productoService;
	private final UsuarioService usuarioService;

	@ModelAttribute("seleccionados")
	public List<ProductoSeleccionado> inicio() {
		return new ArrayList<ProductoSeleccionado>();
	}

	@GetMapping("listado")
	public String listado(Model model) {
		model.addAttribute("lstBoletas", boletaService.getAll());
		return "boleta/listado";
	}

	@GetMapping("nuevo")
	public String nuevo(Model model) {
		model.addAttribute("productos", productoService.getAllActive());
		model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
		return "boleta/nuevo";
	}

	@PostMapping("agregar")
	public String agregar(
		@ModelAttribute ProductoSeleccionado productoSeleccionado,
		@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados, 
		Model model
	) {
		model.addAttribute("productos", productoService.getAllActive());
		model.addAttribute("productoSeleccionado", new ProductoSeleccionado());

		// validamos si ya existe producto
		boolean existe = seleccionados.stream().anyMatch(
				p -> p.getIdProducto() == productoSeleccionado.getIdProducto()
		);

		if (existe) {
			model.addAttribute("alert", Alert.sweetAlertInfo("El producto se encuentra en la lista"));
			return "boleta/nuevo";
		}

		seleccionados.add(productoSeleccionado);
		return "boleta/nuevo";
	}

	@PostMapping("remover")
	public String remover(@RequestParam Integer id,
		@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados, 
		Model model
	) {

		seleccionados.removeIf(p -> p.getIdProducto().equals(id));

		model.addAttribute("productos", productoService.getAllActive());
		model.addAttribute("productoSeleccionado", new ProductoSeleccionado());
		return "boleta/nuevo";
	}

	@PostMapping("/registrar")
	public String registrar(
		@ModelAttribute Boleta boleta,
		@ModelAttribute("seleccionados") List<ProductoSeleccionado> seleccionados, 
		Model model,
		RedirectAttributes flash,
		HttpSession session
	) {
		model.addAttribute("productos", productoService.getAllActive());
		model.addAttribute("productoSeleccionado", new ProductoSeleccionado());

		// Obtenemos dato de sesión
		var idUsuario = (Integer) session.getAttribute("idUsuario");

		// Validamos sesión
		if (idUsuario == null) {
			flash.addFlashAttribute("alert", Alert.sweetAlertInfo("Su sesión ha expirado"));
			return "redirect:/";
		}

		// obtenemos al usuario y lo seteamos
		var usuario = usuarioService.getOne(idUsuario);
		boleta.setUsuario(usuario);

		// Validamos que al menos haya un seleccionado
		if (seleccionados.stream().count() == 0) {
			model.addAttribute("alert", Alert.sweetAlertInfo("Agregue como min 1 producto"));
			return "boleta/nuevo";
		}

		// Mapeamos los datos seleccionados a DetalleBoleta y lo seteamos
		var lstDetalleBoleta = seleccionados.stream().map(
			item -> {
				var detalle = new DetalleBoleta();
				detalle.setBoleta(boleta);
				detalle.setProducto(productoService.getOne(item.getIdProducto()));
				detalle.setCantidad(item.getCantidad());
				detalle.setPrecioVenta(item.getPrecio());
				return detalle;
			}
		).toList();

		// Seteamos el listado del detalle y registramos la nueva boleta
		boleta.setLstDetalleBoleta(lstDetalleBoleta);
		
		var response = boletaService.create(boleta);
		flash.addFlashAttribute("toast", Alert.sweetToast(response.mensaje(), "success", 5000));		
		return "redirect:/boleta/listado";
	}
}
