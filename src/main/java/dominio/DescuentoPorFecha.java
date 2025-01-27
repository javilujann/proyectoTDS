package dominio;

import java.time.LocalDateTime;

public class DescuentoPorFecha implements Descuento{
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private LocalDateTime fechaRegistro;
	
	
	
	public DescuentoPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDateTime fechaRegistro) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaRegistro = fechaRegistro;
	}


	@Override
	public float aplicarDescuento(float precioBase, float porcentaje) {
		if(fechaRegistro.isAfter(fechaFin)) return precioBase;
		if(fechaRegistro.isBefore(fechaInicio)) return precioBase;
		return precioBase*(1-porcentaje/100);
	}	

}
