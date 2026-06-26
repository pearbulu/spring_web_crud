package com.ciberfarma.model;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_usuario")
@Getter
@Setter
@DynamicInsert
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nombres")
	private String nombres;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "cuenta")
	private String cuenta;

	@Column(name = "clave")
	private String clave;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha_nac")
	private LocalDate fechaNac;

	@Column(name = "activo")
	private Boolean activo;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo")
	private Tipo tipo;

	public String getFullName() {
		return String.format("%s %s", nombres, apellidos);
	}
	
	public String getActivoDescripcion() {
		return activo ? "Activo" : "Inactivo";
	}
}
