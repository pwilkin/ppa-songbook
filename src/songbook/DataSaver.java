package songbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import songbook.data.Album;
import songbook.data.Artist;
import songbook.data.Song;
import songbook.data.Songbook;

/**
 * Created by pwilkin on 13-Dec-18.
 */
public class DataSaver {

    public Songbook readSongbook(InputStream is) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        Songbook book = new Songbook();
        book.setArtists(new ArrayList<>());
        String line;
        boolean readingArtistName = false;
        boolean readingAlbumName = false;
        boolean readingSong = false;
        Artist currentArtist = null;
        Album currentAlbum = null;
        try {
            while ((line = bf.readLine()) != null) {
                if (readingArtistName) {
                    currentArtist = new Artist();
                    currentArtist.setName(line);
                    currentArtist.setAlbums(new ArrayList<>());
                    book.getArtists().add(currentArtist);
                    readingArtistName = false;
                } else if (readingAlbumName) {
                    currentAlbum = new Album();
                    currentAlbum.setName(line);
                    currentAlbum.setSongs(new ArrayList<>());
                    currentArtist.getAlbums().add(currentAlbum);
                    readingAlbumName = false;
                } else if (readingSong) {
                    currentAlbum.getSongs().add(readSong(line));
                    readingSong = false;
                } else {
                    if ("###".equals(line)) {
                        readingArtistName = true;
                    } else if ("##".equals(line)) {
                        readingAlbumName = true;
                    } else if ("#".equals(line)) {
                        readingSong = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return book;
    }

    private Song readSong(String line) {
        boolean readingTitle = true;
        boolean inEscape = false;
        StringBuilder current = new StringBuilder();
        Song song = new Song();
        for (int i = 0; i < line.length(); i++) {
            String cur = line.substring(i, i + 1);
            if (inEscape) {
                current.append(cur);
                inEscape = false;
            } else {
                if ("\\".equals(cur)) {
                    inEscape = true;
                } else if ("#".equals(cur)) {
                    if (readingTitle) {
                        song.setTitle(current.toString());
                        song.setLyrics("");
                        readingTitle = false;
                    } else {
                        song.setLyrics(song.getLyrics() + current.toString() + "\n");
                    }
                    current = new StringBuilder();
                } else {
                    current.append(cur);
                }
            }
        }
        song.setLyrics(song.getLyrics() + current.toString());
        return song;
    }

}
