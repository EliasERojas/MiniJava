import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// para probar esto, hice 
// seq 1003 >> $HOME/bulk.txt 
// y en la raiz del proyecto
// chattr +i $(seq 100 400)

public class App {
	private static final String LOG_FILE = "salida.log";
	private static final String BULK_PATH = System.getProperty("user.home") + "/bulk.txt";
	private static final int MILISEC_TO_SEC = 1000;
	private static final int SEC_TO_MIN = 60;
	private static final int MAX_FILES_TO_PROCESS = 10;
	private static final int MB = 1024;
	private String bulkPath;
	private String logPath;

	public static void main(String[] args) {
		App app = new App(BULK_PATH,LOG_FILE);
		app.runApp();
	}
		
	public void runApp(){
		while (true) {
			try {
				Path logPathObj = Paths.get(logPath);
				if(Files.exists(logPathObj) && Files.size(logPathObj) >= MB / 64){
					Files.delete(logPathObj);
				}
				cycle();
				Thread.sleep(MILISEC_TO_SEC * SEC_TO_MIN);
			} catch (InterruptedException iexc) {
				System.err.println("Couldn't send thread to sleep");
				iexc.printStackTrace();
			} catch (IOException ioexc) {
				System.err.println("Couldn't delete file or get size");
				ioexc.printStackTrace();
			}
		}
	}
	
	public App(String bulkPath, String logPath) {
		this.bulkPath = bulkPath;
		this.logPath = logPath;
	}

	public App(String bulkPath) {
		this(bulkPath, LOG_FILE);
	}
	
	public App() {
		this(BULK_PATH);
	}

	private void cycle() {
		FileReader bulkFile = null;
		BufferedReader bulkReader = null;
		try {
			bulkFile = new FileReader(bulkPath);
			bulkReader = new BufferedReader(bulkFile);
			ArrayList<String> filePathsArray = new ArrayList<>();
			String filePath;
			FilesProcessor fp;
			while ((filePath = bulkReader.readLine()) != null) {
				filePathsArray.add(filePath);
				if (filePathsArray.size() >= MAX_FILES_TO_PROCESS) {
					fp = new FilesProcessor(filePathsArray,logPath);
					fp.runFileProcessor();
					filePathsArray = new ArrayList<>();
				}
			}
			if (!filePathsArray.isEmpty()) {
				fp = new FilesProcessor(filePathsArray,logPath);
				fp.runFileProcessor();
			}
		} catch (IOException ioexc) {
			System.err.println("Can't read bulk file, it might not be at $HOME");
			ioexc.printStackTrace();
		} finally {
			if (bulkFile != null) {
				try {
					bulkFile.close();
				} catch (IOException ioexc) {
					System.err.println("Couldn't close file reader");
					ioexc.printStackTrace();
				}
			}
			if (bulkReader != null) {
				try {
					bulkReader.close();
				} catch (IOException ioexc) {
					System.err.println("Couldn't close bulk reader");
					ioexc.printStackTrace();
				}
			}
		}
	}
}
