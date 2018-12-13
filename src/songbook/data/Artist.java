package songbook.data;

import java.util.List;

/**
 * Created by pwilkin on 13-Dec-18.
 */
public class Artist {

    protected String name;
    protected List<Album> albums;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
