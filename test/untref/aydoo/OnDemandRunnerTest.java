package untref.aydoo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class OnDemandRunnerTest {

	private static final String INPUT_DIR = "resourcesTests/dirWithZipsToProcess";
	private static final String GENERATED_YAML = "resourcesTests/dirWithZipsToProcess/output.yml";

	@After
	public void tearDown() throws Exception {
		File dir = new File(INPUT_DIR);
		for (File eachDir : dir.listFiles()) {
			if (eachDir.exists() && eachDir.isDirectory()) {
				for (File file : eachDir.listFiles()) {
					file.delete();
				}
				eachDir.delete();
			}
		}
		File yaml = new File(GENERATED_YAML);
		if (yaml.exists()) {
			yaml.delete();
		}
	}

	@Test
	public void testIntegracionOnDemandRunner() {
		new OnDemandRunner(INPUT_DIR);

		String yamlContentExpected = "--- # estadisticas de uso de bicicletas\n"
				+ "bicicleta-utilizada-mas-veces: [430, 443]\n"
				+ "bicicleta-utilizada-menos-veces: [361, 369, 37, 370, 42, 428, 444, 446, 48, 482, 483]\n"
				+ "recorrido-mas-veces-realizado: [[7, 3]]\n"
				+ "tiempo-promedio-de-uso: 28\n";

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
