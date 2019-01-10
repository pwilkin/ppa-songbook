package songbook;

import songbook.data.Songbook;

/**
 * Created by pwilkin on 10-Jan-19.
 */
public class DataStorage {

    protected Songbook songbook;

    public Songbook getSongbook() {
        return songbook;
    }

    public void setSongbook(Songbook songbook) {
        this.songbook = songbook;
    }
}
