package songbook.data;

import java.util.List;

/**
 * Created by pwilkin on 13-Dec-18.
 */
public class Songbook {

    protected List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (artists != null) {
            for (int i = 0; i < artists.size(); i++) {
                sb.append(artists.get(i));
                sb.append("\n---\n");
            }
        }
        return sb.toString();
    }
}
