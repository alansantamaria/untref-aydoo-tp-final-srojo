package untref.aydoo.runtime;

import org.apache.log4j.Logger;

import untref.aydoo.domain.Prestamo;
import untref.aydoo.domain.StatsCalculator;
import untref.aydoo.exceptions.RegistroHeaderException;
import untref.aydoo.exceptions.RegistroInvalidoException;
import untref.aydoo.files.CsvReader;

public class Processor implements Runnable {

	private Logger logger = Logger.getLogger("log");
	private StatsCalculator calculador;
	private String csvFile;

	public Processor(StatsCalculator calculador, String csvFile) {
		this.calculador = calculador;
		this.csvFile = csvFile;
	}

	@Override
	public void run() {
		CsvReader reader = new CsvReader(csvFile);
		Prestamo prestamo = new Prestamo();
		String line;

		while ((line = reader.readLine()) != null) {
			try {
				prestamo.parse(line);
				logger.debug("Registro procesado" + prestamo.toString());
				calculador.addPrestamo(prestamo);
			} catch (RegistroInvalidoException e) {
				logger.warn(e.getMessage());
			} catch (RegistroHeaderException e) {
				logger.debug("Se procesa el encabezado del archivo.");
			}
		}
	}

}
