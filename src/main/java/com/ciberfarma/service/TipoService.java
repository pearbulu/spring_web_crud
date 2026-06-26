package com.ciberfarma.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciberfarma.model.Tipo;
import com.ciberfarma.repository.TipoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoService {
	
	private final TipoRepository tipoRepository;
	
	public List<Tipo> getAll() {
		return tipoRepository.findAll();
	}
}
