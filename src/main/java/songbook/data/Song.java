package songbook.data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by pwilkin on 13-Dec-18.
 */
@Entity
public class Song {

    @Id
    protected Integer id;
    protected String title;
    protected String lyrics;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return title + "\n\n" + lyrics + "\n";
    }
}
