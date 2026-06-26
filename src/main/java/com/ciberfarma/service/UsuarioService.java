package com.ciberfarma.service;

import org.springframework.stereotype.Service;

import com.ciberfarma.model.Usuario;
import com.ciberfarma.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	public final UsuarioRepository usuarioRepository;
	
	public Usuario getOne(Integer id) {
		return usuarioRepository.findById(id).orElse(null);
	}
}
