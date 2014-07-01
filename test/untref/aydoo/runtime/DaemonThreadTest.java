package untref.aydoo.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import untref.aydoo.files.FileManager;
import untref.aydoo.runtime.DaemonThread;

public class DaemonThreadTest {

	private static final String INPUT_DIR = "resourcesTests/dirWithZipsToProcessDaemonThread";
	private static final String GENERATED_YAML = "resourcesTests/dirWithZipsToProcessDaemonThread/recorridosA.yml";
	private static final String INPROCESS_DIR = "resourcesTests/dirWithZipsToProcessDaemonThread/inprocess";
	private static final String INPROCESS_ZIP_FILE = "resourcesTests/dirWithZipsToProcessDaemonThread/inprocess/recorridosA.zip";
	private static final String PROCESSED_ZIP_FILE = "resourcesTests/dirWithZipsToProcessDaemonThread/processed/recorridosA.zip";

	@After
	public void tearDown() {
		FileManager manager = new FileManager();
		manager.moveFileTo(new File(PROCESSED_ZIP_FILE), new File(
				INPROCESS_DIR));
		File yaml = new File(GENERATED_YAML);
		if (yaml.exists()) {
			yaml.delete();
		}
	}

	@Test
	public void testIntegracionOnDemandRunner() {
		DaemonThread daemonThread = new DaemonThread(INPUT_DIR,
				INPROCESS_ZIP_FILE);

		String yamlContentExpected = "--- # estadisticas de uso de bicicletas\n"
				+ "bicicleta-utilizada-mas-veces: [403]\n"
				+ "bicicleta-utilizada-menos-veces: [339, 346, 351, 358, 370, 441, 460, 478]\n"
				+ "recorrido-mas-veces-realizado: [[7, 3]]\n"
				+ "tiempo-promedio-de-uso: 22\n"
				+ "bicicletas-mas-tiempo-utilizadas:{430=85}\n";

		daemonThread.run();

		String yamlContent = getYamlContent();

		Assert.assertEquals(yamlContentExpected, yamlContent);
	}

	private String getYamlContent() {
		String content = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(GENERATED_YAML)));
			while ((line = reader.readLine()) != null) {
				content += (line + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
