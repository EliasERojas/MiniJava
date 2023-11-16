import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FilesProcessor implements Runnable {
	private static final boolean APPEND = true;
	private List<String> filePathsArray;
	private String logFilePath;

	public FilesProcessor(List<String> filePathsArray, String logFilePath) {
		this(filePathsArray);
		if (logFilePath == null) {
			throw new NullPointerException("Can't send null path as argument");
		}
		this.logFilePath = logFilePath;
	}

	public FilesProcessor(List<String> filePathsArray) {
		if (filePathsArray == null) {
			throw new NullPointerException("Can't send a null list as argument");
		}
		this.filePathsArray = filePathsArray;
	}

	public void runFileProcessor() {
		Thread t = new Thread(this);
		t.start();
	}

	private synchronized void writeLog(String exceptionFileAbsolutePath) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(logFilePath, APPEND);
			fw.append(exceptionFileAbsolutePath + '\n');
		} catch (IOException ioexc) {
			ioexc.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException ioexc) {
					System.err.println("Couldn't close log file");
					ioexc.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		for (String filePath : filePathsArray) {
			File fileToProcess = new File(filePath);
			if (fileToProcess.isDirectory()) {
				System.err.println("Can't process directories");
				return;
			}
			boolean fileCreated = false;
			try {
				fileCreated = fileToProcess.createNewFile();
				if (!fileCreated) {
					Files.delete(fileToProcess.toPath());
				}
			} catch (IOException ioexc) {
				if(logFilePath != null) {
					writeLog((fileCreated ? "C - " : "B - ") + fileToProcess.getAbsolutePath());
				}
				ioexc.printStackTrace();
			}
		}
	}
}
