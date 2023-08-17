import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class App {

    private boolean backup(String savingPath, String destionationPath) throws IOException, FileNotFoundException {

        File folder = new File(savingPath);

        if (folder.exists()) {
            if (folder.isDirectory()) {

                // creazione della directory di backup
                String backupDirPath = destionationPath + "\\backup_"
                        + new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
                File backupDir = new File(backupDirPath);

                if (backupDir.mkdir()) {
                    saveFiles(folder, backupDir);
                } else
                    throw new IOException("Non è stato possibile creare la directory di backup");

            } else
                throw new IOException("Il path passato non e' di una directory");
        } else
            throw new IOException("Il path passato non esiste");

        return false;
    }

    private void saveFiles(File originalDir, File backupDir) throws FileNotFoundException, IOException {
        for (File file : originalDir.listFiles()) {
            System.out.println(file.getName());

            if (file.isDirectory()) {
                File backupSubDir = new File(backupDir + "\\" + file.getName() + "\\");
                if(backupSubDir.mkdir()) {
                    saveFiles(file, backupSubDir);
                    backupSubDir.setLastModified(file.lastModified());
                }
                else 
                    throw new IOException("Non è stato possibile creare una sottodirectory");
            }
            else {
                String pathNewFile = backupDir + "\\" + file.getName();
                File f = new File(pathNewFile);
                FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
                FileOutputStream outputStream = new FileOutputStream(pathNewFile);
                DeflaterOutputStream compresser = new DeflaterOutputStream(outputStream);

                int contents;
                while((contents=inputStream.read()) != -1) {
                    compresser.write(contents);
                }
                compresser.close();
                f.setLastModified(file.lastModified());
            }
        }
    }

    public static void main(String[] args) {
        App fileAssert = new App();
        Path savingPath = Paths.get("c:\\Users\\miche\\ICon-2022-2023\\");
        Path destPath = Paths.get("c:\\Users\\miche\\Desktop\\");

        try {
            fileAssert.backup(savingPath.toString(), destPath.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}