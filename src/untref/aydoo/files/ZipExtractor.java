package untref.aydoo.files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class ZipExtractor {

	private Logger logger = Logger.getLogger("log");

	public List<String> extract(String zipFile, String outputPath) {
		ZipInputStream zis;
		ZipEntry ze;
		OutputStream os = null;
		List<String> fileList = new ArrayList<String>();
		String filePath;

		outputPath = createDir(outputPath);

		try {
			zis = new ZipInputStream(new BufferedInputStream(
					new CheckedInputStream(new FileInputStream(zipFile),
							new Adler32())));

			while ((ze = zis.getNextEntry()) != null) {
				filePath = getFreeFilePath(outputPath, ze.getName());
				os = new FileOutputStream(filePath);
				fileList.add(filePath);
				int readed;
				byte [] buffer = new byte[1024];
				while (0 < (readed = zis.read(buffer))){
					os.write(buffer,0, readed);
				}
				
			}

			os.close();
			zis.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return fileList;
	}

	private String createDir(String dirName) {
		File dir = new File(dirName);

		if (!dir.exists()) {
			dir.mkdir();
		}

		return dir.getAbsolutePath();
	}

	private String getFreeFilePath(String baseDir, String originalName) {
		String name = "" + System.currentTimeMillis() / 1000L;
		String extension = getExtension(originalName);
		File file = new File(baseDir + File.separator + name + "." + extension);
		while (file.exists()) {
			name += "_";
			file = new File(baseDir + File.separator + name + "." + extension);
		}
		return baseDir + File.separator + name + "." + extension;
	}

	private String getExtension(String name) {
		return name.substring(name.lastIndexOf(".") + 1, name.length());
	}

}
