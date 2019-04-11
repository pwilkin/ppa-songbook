package songbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import songbook.data.Songbook;

/**
 * Created by pwilkin on 10-Jan-19.
 */
public class DataStorage {

    protected Songbook songbook;
    protected File selectedFile;

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public Songbook getSongbook() {
        return songbook;
    }

    public void setSongbook(Songbook songbook) {
        this.songbook = songbook;
    }

    public void saveData() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
            new DataSaver().writeSongbook(songbook, fos);
        }
    }

    public void saveToPdf() {
        String userHomeDir = System.getProperty("user.home");
        Path homeDir = Paths.get(userHomeDir);
        Path subdir = homeDir.resolve(".songbook");
        Path songsFile = subdir.resolve("songbook.pdf");
        String absPath = songsFile.toAbsolutePath().toString();
        savePdfToFile(absPath);
    }

    public void savePdfToFile(String absPath) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(absPath));
            new DataSaver().exportSongbookToPdf(songbook, pdfDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDataToDB() {
        new DataSaver().writeSongbookToDatabase(songbook);
    }

    public void loadDataFromDB() {
        songbook = new DataSaver().readSongbookFromDatabase();
    }
}