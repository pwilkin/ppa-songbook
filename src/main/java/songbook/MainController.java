package songbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import songbook.DataSaver.DataException;
import songbook.data.Songbook;

public class MainController {

    @FXML
    protected Button chooser;

    @FXML
    protected Button processor;

    @FXML
    protected Button def;

    protected DataStorage dataStorage = new DataStorage();

    public void showChooseDialog(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(chooser.getScene().getWindow());
        if (file != null && file.exists()) {
            dataStorage.setSelectedFile(file);
            processor.setDisable(false);
            chooser.setText(file.getAbsolutePath());
        }
    }

    public void process(ActionEvent actionEvent) {
        Stage stage = (Stage) chooser.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("songbook.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Input/output exception: " + e.getMessage());
            alert.show();
        }
        stage.setTitle("Songbook");
        stage.setScene(new Scene(root, 897, 570));
        stage.show();
        stage.centerOnScreen();
        SongbookController sc = loader.getController();
        sc.setDataStorage(dataStorage);
    }

    public void loadData() {
        try (FileInputStream fis = new FileInputStream(dataStorage.getSelectedFile())) {
            Songbook songbook = new DataSaver().readSongbook(fis);
            dataStorage.setSongbook(songbook);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("File not found: " + e.getMessage());
            alert.show();
        } catch (DataException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Data error: " + e.getMessage());
            alert.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Input/output exception: " + e.getMessage());
            alert.show();
        }
    }

    public void loadDefault(ActionEvent actionEvent) {
        String userHomeDir = System.getProperty("user.home");
        Path homeDir = Paths.get(userHomeDir);
        Path subdir = homeDir.resolve(".songbook");
        Path songsFile = subdir.resolve("songs.txt");
        try {
            if (!Files.isDirectory(subdir)) {
                Files.createDirectory(subdir);
            }
            if (!Files.exists(songsFile)) {
                Files.createFile(songsFile);
            }
            dataStorage.setSelectedFile(songsFile.toFile());
            processor.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void migrateDefault(ActionEvent actionEvent) {
        loadDefault(actionEvent);
        loadData();
        dataStorage.saveDataToDB();
    }

    public void loadFromDB(ActionEvent actionEvent) {
        dataStorage.loadDataFromDB();
        processor.setDisable(false);
    }
}
