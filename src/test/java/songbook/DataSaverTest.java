package songbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import songbook.DataSaver.DataException;
import songbook.data.Album;
import songbook.data.Artist;
import songbook.data.Song;
import songbook.data.Songbook;

/**
 * Created by pwilkin on 28-Feb-19.
 */
public class DataSaverTest {

    @Test
    public void testReadSimpleSongbook() {
        String input = "###\nTestowy\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        try {
            Songbook sb = new DataSaver().readSongbook(bais);
            checkArtistsNotEmpty(sb);
            Assert.assertEquals(sb.getArtists().get(0).getName(), "Testowy");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkArtistsNotEmpty(Songbook sb) {
        Assert.assertNotNull(sb);
        Assert.assertNotNull(sb.getArtists());
        Assert.assertTrue(sb.getArtists().size() > 0);
        Assert.assertNotNull(sb.getArtists().get(0));
    }

    private void checkAlbumsNotEmpty(Artist art) {
        Assert.assertNotNull(art);
        Assert.assertNotNull(art.getAlbums());
        Assert.assertTrue(art.getAlbums().size() > 0);
        Assert.assertNotNull(art.getAlbums().get(0));
    }

    private void checkSongsNotEmpty(Album alb) {
        Assert.assertNotNull(alb);
        Assert.assertNotNull(alb.getSongs());
        Assert.assertTrue(alb.getSongs().size() > 0);
        Assert.assertNotNull(alb.getSongs().get(0));
    }

    @Test
    public void testReadASong() {
        String input = "###\nTestowy\n##\nTest album\n#\nPomidor#Jestem pomidorem#jejeeee\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        try {
            Songbook sb = new DataSaver().readSongbook(bais);
            checkArtistsNotEmpty(sb);
            Artist firstArtist = sb.getArtists().get(0);
            checkAlbumsNotEmpty(firstArtist);
            Album firstAlbum = firstArtist.getAlbums().get(0);
            checkSongsNotEmpty(firstAlbum);
            Song firstSong = firstAlbum.getSongs().get(0);
            Assert.assertEquals(firstSong.getTitle(), "Pomidor");
            Assert.assertTrue(firstSong.getLyrics().contains("jejeee"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWriteASong() {
        // ...
        ByteArrayOutputStream baos = null; // zmienić!
        String fromBaos = baos.toString();
    }

    @Test(expected = DataException.class)
    public void testDoNotReadASong() throws DataException {
        String input = "Testowy\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        try {
            Songbook sb = new DataSaver().readSongbook(bais);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
