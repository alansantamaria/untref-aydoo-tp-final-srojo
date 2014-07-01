package untref.aydoo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import untref.aydoo.exceptions.RegistroHeaderException;
import untref.aydoo.exceptions.RegistroInvalidoException;

public class StatsCalculatorTest {

	private Prestamo prestamo = new Prestamo();

	@Test
	public void elCalculadorDebeSaberCualSonLasBicicletasMasUsadas()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;13");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;445;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;3");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;7");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;441;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;5");
		calculador.addPrestamo(prestamo);

		List<String> bicicletasEsperadas = new ArrayList<String>();
		bicicletasEsperadas.add("442");
		bicicletasEsperadas.add("443");
		Collections.sort(bicicletasEsperadas);

		List<String> bicicletasMasUsadas = calculador.getBicicletasMasUsadas();
		Collections.sort(bicicletasMasUsadas);

		Assert.assertEquals(bicicletasEsperadas, bicicletasMasUsadas);
	}

	@Test
	public void elCalculadorDebeSaberCualesSonLasBicicletasMenosUsadas()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;13");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;445;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;3");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;7");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;441;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;5");
		calculador.addPrestamo(prestamo);

		List<String> bicicletasEsperadas = new ArrayList<String>();
		bicicletasEsperadas.add("441");
		bicicletasEsperadas.add("445");
		Collections.sort(bicicletasEsperadas);

		List<String> bicicletasMenosUsadas = calculador
				.getBicicletasMenosUsadas();
		Collections.sort(bicicletasMenosUsadas);

		Assert.assertEquals(bicicletasEsperadas, bicicletasMenosUsadas);
	}

	@Test
	public void debeSaberCalcularElTiempoPromedioDeUso()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;13");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;445;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;3");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;7");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;441;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;5");
		calculador.addPrestamo(prestamo);

		int tiempoPromedioUsoEsperado = 9;

		Assert.assertEquals(tiempoPromedioUsoEsperado,
				calculador.getTiempoPromedioUso());
	}

	@Test
	public void elCalculadorDebeSaberCualesSonLosRecorridosMasUsados()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;13");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;445;2010-12-30 19:34:16;3;RETIRO;2010-12-30 19:47:03;5;ADUANA;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;3");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;7;PLAZA ROMA;2010-12-30 19:17:44;3;RETIRO;7");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;3;RETIRO;2010-12-30 19:47:03;5;ADUANA;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;441;2010-12-30 19:34:16;6;DERECHO;2010-12-30 19:46:03;3;RETIRO;5");
		calculador.addPrestamo(prestamo);

		List<Recorrido> recorridosEsperados = new ArrayList<Recorrido>();
		recorridosEsperados.add(new Recorrido("5", "3"));
		recorridosEsperados.add(new Recorrido("3", "5"));
		Collections.sort(recorridosEsperados);

		List<Recorrido> recorridosMasUsados = calculador
				.getRecorridosMasUsados();
		Collections.sort(recorridosMasUsados);

		Assert.assertEquals(recorridosEsperados, recorridosMasUsados);
	}

	@Test
	public void elCalculadorDeberiaFuncionarAunqueNoHayaDatos()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		
		List<String> listaBicicletasVacia = new ArrayList<String>();
		List<Recorrido> listaRecorridosVacia = new ArrayList<Recorrido>();
		int tiempoPromedioEsperado = 0;
		
		Assert.assertEquals(listaBicicletasVacia, calculador.getBicicletasMasUsadas());
		Assert.assertEquals(listaBicicletasVacia, calculador.getBicicletasMenosUsadas());
		Assert.assertEquals(listaRecorridosVacia, calculador.getRecorridosMasUsados());
		Assert.assertEquals(tiempoPromedioEsperado, calculador.getTiempoPromedioUso());
	}
	
	@Test
	public void debeSaberCalcularLasBicicletasConMasTiempoDeUso()
			throws RegistroInvalidoException, RegistroHeaderException {
		StatsCalculator calculador = new StatsCalculator();
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;13");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;445;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;21");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;443;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;3");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;7");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;442;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;14");
		calculador.addPrestamo(prestamo);
		prestamo.parse("2722;441;2010-12-30 19:34:16;5;ADUANA;2010-12-30 19:47:03;3;RETIRO;5");
		calculador.addPrestamo(prestamo);
		
		Map<String, Integer> salidaEsperada = new HashMap<String, Integer>();
		salidaEsperada.put("445", 21);
		salidaEsperada.put("442", 21);
		
		Assert.assertEquals(salidaEsperada, calculador.getBicicletasUsadasMasTiempo());

	}

}
