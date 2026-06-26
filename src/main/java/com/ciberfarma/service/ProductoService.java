package com.ciberfarma.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciberfarma.dto.ProductoFilter;
import com.ciberfarma.dto.ResultadoResponse;
import com.ciberfarma.model.Producto;
import com.ciberfarma.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

	private final ProductoRepository productoRepository;

	public List<Producto> getAll() {
		return productoRepository.findAllByOrderByIdProductoDesc();
	}

	public List<Producto> getAllActive() {
		return productoRepository.findAllByActivoTrue();
	}

	public List<Producto> search(ProductoFilter filter) {
		return productoRepository.findAllByFilters(filter.getIdCategoria(), filter.getIdProveedor());
	}

	public ResultadoResponse create(Producto producto) {

		var existe = productoRepository.existsByDescripcion(producto.getDescripcion());

		if (existe) {
			return new ResultadoResponse(false, "Ya existe un producto con esa descripción.");
		}

		try {
			var registro = productoRepository.save(producto);
			var mensaje = String.format("Producto con Id %s registrado", registro.getIdProducto());

			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	public Producto getOne(Integer idProducto) {
		return productoRepository.findById(idProducto).orElseThrow();
	}

	public ResultadoResponse update(Producto producto) {

		var existe = productoRepository.existsByDescripcionAndIdProductoNot(producto.getDescripcion(), producto.getIdProducto());

		if (existe) {
			return new ResultadoResponse(false, "Ya existe un producto con esa descripción.");
		}

		try {
			var registro = productoRepository.save(producto);
			var mensaje = String.format("Producto con Id %s actualizado", registro.getIdProducto());

			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

	@Transactional
	public ResultadoResponse changeActive(Integer id) {
		var producto = productoRepository.findById(id).orElseThrow();

		try {
			producto.setActivo(!producto.getActivo());

			var estado = producto.getActivo() ? "activado" : "desactivado";
			var mensaje = String.format("Producto con Id %s %s", producto.getIdProducto(), estado);

			return new ResultadoResponse(true, mensaje);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Hubo un error en la transacción");
		}
	}

}
