package com.ciberfarma.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciberfarma.dto.ResultadoResponse;
import com.ciberfarma.dto.UsuarioFilter;
import com.ciberfarma.model.Producto;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	public final UsuarioRepository usuarioRepository;
	
	public List<Usuario> search(UsuarioFilter filter) {
		return usuarioRepository.findAllByFilters(filter.getIdTipo());
	}
	
	public ResultadoResponse create(Usuario producto) {
		try {
			var registro = usuarioRepository.save(producto);
			var mensaje = String.format("Usuario con Id %s registrado", registro.getIdUsuario());
			
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
	
	public Usuario getOne(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}
	
	public ResultadoResponse update(Usuario usuario) {
		try {
			var registro = usuarioRepository.save(usuario);
			var mensaje = String.format("Usuario con Id %s actualizado", registro.getIdUsuario());
			
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}
	
	@Transactional
	public ResultadoResponse changeActive(Integer id) {
		var usuario = usuarioRepository.findById(id).orElse(null);
		
		try {
			usuario.setActivo(!usuario.getActivo());
			
			var estado = usuario.getActivo() ? "activado" : "desactivado";
			var mensaje = String.format("Usuario con Id %s %s", usuario.getIdUsuario(), estado);
			
			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}	

}
