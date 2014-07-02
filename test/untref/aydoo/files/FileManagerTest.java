package untref.aydoo.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import untref.aydoo.files.FileManager;

public class FileManagerTest {

	private static final String INPUT_DIR = "resourcesTests"+File.separator+"dirWithZips";
	private static final String TEST_DIR_EXISTS = "resourcesTests"+File.separator+"testExists";
	private static final String CSV_EXT = "CSV";
	private static final String TEST_DIR_NOT_EXISTS = "resourcesTests"+File.separator+"testNotExists";

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
	}

	@Test
	public void dadaUnaListaDeZipsDeberiaDejarTodosLosArchivosCsvListosParaProcesar() {
		FileManager fileManager = new FileManager();

		List<String> zipList = new ArrayList<String>();
		zipList.add(INPUT_DIR + File.separator + "recorridosA.zip");
		zipList.add(INPUT_DIR + File.separator + "recorridosB.zip");
		zipList.add(INPUT_DIR + File.separator + "recorridosC.zip");

		String dirToProcess = fileManager.prepareFiles(INPUT_DIR, zipList);

		File outputDir = new File(dirToProcess);
		Assert.assertTrue(outputDir.exists());
		Assert.assertTrue(outputDir.isDirectory());
	}

	@Test
	public void dadoUnDirectorioYUnaExtensionDeberiaDevolverLaListaDeArchivosValidos() {
		FileManager fileManager = new FileManager();

		List<String> files = fileManager.getFilesByExtension(TEST_DIR_EXISTS,
				CSV_EXT);
		Collections.sort(files);

		List<String> filesExpected = new ArrayList<String>();
		filesExpected.add("resourcesTests"+File.separator+"testExists"+File.separator+"recorridos1.csv");
		filesExpected.add("resourcesTests"+File.separator+"testExists"+File.separator+"recorridos2.csv");
		filesExpected.add("resourcesTests"+File.separator+"testExists"+File.separator+"recorridos3.csv");
		filesExpected.add("resourcesTests"+File.separator+"testExists"+File.separator+"recorridos4.csv");
		Collections.sort(filesExpected);

		Assert.assertEquals(filesExpected, files);
	}

	@Test
	public void dadoUnDirectorioYUnaExtensionDeberiaDevolverUnaListaVaciaSiNoHayArchivosDeDichaExtension() {
		FileManager fileManager = new FileManager();

		List<String> files = fileManager.getFilesByExtension(TEST_DIR_EXISTS,
				"pdf");

		List<String> filesExpected = new ArrayList<String>();

		Assert.assertEquals(filesExpected, files);
	}

	@Test
	public void dadoUnDirectorioInexistenteDeberiaDevolverNull() {
		FileManager fileManager = new FileManager();

		List<String> files = fileManager.getFilesByExtension(
				TEST_DIR_NOT_EXISTS, CSV_EXT);

		Assert.assertNull(files);
	}

}
