package untref.aydoo.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class StatsCalculator {

	private Logger logger = Logger.getLogger("log");

	private int contadorDeRegistros = 0;
	private Map<String, Integer> contadorPorBicicleta = new HashMap<String, Integer>();
	private Map<Recorrido, Integer> contadorPorRecorrido = new HashMap<Recorrido, Integer>();
	private Map<String, Integer> tiempoPorBicicleta = new HashMap<String, Integer>();
	private long acumuladorDeTiempos = 0L;

	public synchronized void addPrestamo(Prestamo prestamo) {
		updateContadorPorBicicleta(prestamo);
		updateContadorPorRecorrido(prestamo);
		updateTiempoPorBicicleta(prestamo);
		updateAcumuladorDeTiempos(prestamo);

		contadorDeRegistros++;
	}

	private void updateContadorPorBicicleta(Prestamo prestamo) {
		Integer cantidadUsos = contadorPorBicicleta.get(prestamo
				.getBicicletaId());
		if (cantidadUsos != null) {
			contadorPorBicicleta.put(prestamo.getBicicletaId(),
					cantidadUsos + 1);
		} else {
			contadorPorBicicleta.put(prestamo.getBicicletaId(), 1);
		}
	}

	private void updateContadorPorRecorrido(Prestamo prestamo) {
		Integer cantidadUsos = contadorPorRecorrido
				.get(prestamo.getRecorrido());
		if (cantidadUsos != null) {
			contadorPorRecorrido.put(prestamo.getRecorrido(), cantidadUsos + 1);
		} else {
			contadorPorRecorrido.put(prestamo.getRecorrido(), 1);
		}
	}

	private void updateTiempoPorBicicleta(Prestamo prestamo) {
		Integer tiempo = tiempoPorBicicleta
				.get(prestamo.getBicicletaId());
		if (tiempo != null) {
			tiempoPorBicicleta.put(prestamo.getBicicletaId(), tiempoPorBicicleta.get(prestamo.getBicicletaId()) + prestamo.getTiempoUso());
		} else {
			tiempoPorBicicleta.put(prestamo.getBicicletaId(), prestamo.getTiempoUso());
		}
	}

	private void updateAcumuladorDeTiempos(Prestamo prestamo) {
		acumuladorDeTiempos += prestamo.getTiempoUso();
	}

	public List<String> getBicicletasMasUsadas() {
		Integer maximo = (contadorPorBicicleta.size() == 0) ? 0
				: contadorPorBicicleta.entrySet().iterator().next().getValue();
		List<String> bicicletas = new ArrayList<String>();
		for (Entry<String, Integer> entry : contadorPorBicicleta.entrySet()) {
			if (entry.getValue() == maximo) {
				bicicletas.add(entry.getKey());
			} else if (entry.getValue() > maximo) {
				maximo = entry.getValue();
				bicicletas = new ArrayList<String>();
				bicicletas.add(entry.getKey());
			}
		}
		Collections.sort(bicicletas);
		return bicicletas;
	}

	public List<String> getBicicletasMenosUsadas() {
		Integer minimo = (contadorPorBicicleta.size() == 0) ? 0
				: contadorPorBicicleta.entrySet().iterator().next().getValue();
		List<String> bicicletas = new ArrayList<String>();
		for (Entry<String, Integer> entry : contadorPorBicicleta.entrySet()) {
			if (entry.getValue() == minimo) {
				bicicletas.add(entry.getKey());
			} else if (entry.getValue() < minimo) {
				minimo = entry.getValue();
				bicicletas = new ArrayList<String>();
				bicicletas.add(entry.getKey());
			}
		}
		Collections.sort(bicicletas);
		return bicicletas;
	}

	public List<Recorrido> getRecorridosMasUsados() {
		Integer maximo = (contadorPorRecorrido.size() == 0) ? 0
				: contadorPorRecorrido.entrySet().iterator().next().getValue();
		List<Recorrido> recorridos = new ArrayList<Recorrido>();
		for (Entry<Recorrido, Integer> entry : contadorPorRecorrido.entrySet()) {
			if (entry.getValue() == maximo) {
				recorridos.add(entry.getKey());
			} else if (entry.getValue() > maximo) {
				maximo = entry.getValue();
				recorridos = new ArrayList<Recorrido>();
				recorridos.add(entry.getKey());
			}
		}
		Collections.sort(recorridos);
		return recorridos;
	}

	public Map<String, Integer> getBicicletasUsadasMasTiempo() {
		Integer maximo = (tiempoPorBicicleta.size() == 0) ? 0
				: tiempoPorBicicleta.entrySet().iterator().next().getValue();
		Map<String, Integer> tiempos = new HashMap<String, Integer>();
		for (Entry<String, Integer> entry : tiempoPorBicicleta.entrySet()) {
			if (entry.getValue() == maximo) {
				tiempos.put(entry.getKey(), entry.getValue());
			} else if (entry.getValue() > maximo) {
				maximo = entry.getValue();
				tiempos = new HashMap<String, Integer>();
				tiempos.put(entry.getKey(), tiempoPorBicicleta.get(entry.getKey()));
			}
		}
		return tiempos;
	}

	public int getTiempoPromedioUso() {
		return (contadorDeRegistros == 0) ? 0 : (int) acumuladorDeTiempos / contadorDeRegistros;
	}

	public void exportYaml(String yamlPath) {
		File file = new File(yamlPath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					file.getAbsolutePath()));
			writer.write(this.toString());
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public String toString() {
		return "--- # estadisticas de uso de bicicletas" + "\n"
				+ "bicicleta-utilizada-mas-veces: " + getBicicletasMasUsadas()
				+ "\n" + "bicicleta-utilizada-menos-veces: "
				+ getBicicletasMenosUsadas() + "\n"
				+ "recorrido-mas-veces-realizado: " + getRecorridosMasUsados()
				+ "\n" + "tiempo-promedio-de-uso: " + getTiempoPromedioUso()
				+ "\n" + "bicicletas-mas-tiempo-utilizadas:" + getBicicletasUsadasMasTiempo();
	}

}
