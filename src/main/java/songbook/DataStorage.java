package songbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        String absPath = selectedFile.getAbsolutePath();
        savePdfToFile(absPath);
    }

    public void savePdfToFile(String absPath) {
        if (absPath.endsWith(".txt")) {
            absPath = absPath.substring(0, absPath.length() - 4) + ".pdf";
        } else {
            throw new IllegalArgumentException("Don't know how to transform file name: " + absPath);
        }
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(absPath));
            new DataSaver().exportSongbookToPdf(songbook, pdfDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}