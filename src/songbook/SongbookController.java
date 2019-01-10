package songbook;

import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import songbook.data.Album;
import songbook.data.Artist;
import songbook.data.Song;

/**
 * Created by pwilkin on 10-Jan-19.
 */
public class SongbookController {

    protected DataStorage dataStorage;
    protected Artist selectedArtist;
    protected Album selectedAlbum;
    protected Song selectedSong;

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        loadArtists();
    }

    private void loadArtists() {
        artists.getItems().clear();
        for (Artist artist : dataStorage.getSongbook().getArtists()) {
            artists.getItems().add(artist.getName());
        }

    }

    private void loadAlbums(String selectedItem) {
        for (Artist artist : dataStorage.getSongbook().getArtists()) {
            if (Objects.equals(artist.getName(), selectedItem)) {
                selectedArtist = artist;
                albums.getItems().clear();
                for (Album album : selectedArtist.getAlbums()) {
                    albums.getItems().add(album.getName());
                }
            }
        }
    }

    private void loadSongs(String selectedItem) {
        for (Album album : selectedArtist.getAlbums()) {
            if (Objects.equals(album.getName(), selectedItem)) {
                selectedAlbum = album;
                songs.getItems().clear();
                for (Song song : selectedAlbum.getSongs()) {
                    songs.getItems().add(song.getTitle());
                }
            }
        }
    }

    private void loadSong(String selectedItem) {
        for (Song song : selectedAlbum.getSongs()) {
            if (Objects.equals(song.getTitle(), selectedItem)) {
                selectedSong = song;
                songText.setText(song.getLyrics());
            }
        }
    }

    public void initialize() {
        artists.getSelectionModel().selectedItemProperty().addListener(c -> {
            loadAlbums(artists.getSelectionModel().getSelectedItem());
        });
        albums.getSelectionModel().selectedItemProperty().addListener(c -> {
            loadSongs(albums.getSelectionModel().getSelectedItem());
        });
        songs.getSelectionModel().selectedItemProperty().addListener(c -> {
            loadSong(songs.getSelectionModel().getSelectedItem());
        });
        songText.textProperty().addListener(c -> {
            selectedSong.setLyrics(songText.getText());
        });
    }

    @FXML
    protected ListView<String> artists;

    @FXML
    protected ListView<String> albums;

    @FXML
    protected ListView<String> songs;

    @FXML
    protected TextArea songText;

    @FXML
    protected Button save;

    public void saveSong(ActionEvent actionEvent) {

    }
}
