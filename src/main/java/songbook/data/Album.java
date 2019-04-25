package songbook.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Created by pwilkin on 13-Dec-18.
 */
@Entity
public class Album {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Integer id;
    @OneToMany(targetEntity = Song.class, cascade = CascadeType.ALL)
    protected List<Song> songs;
    protected String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (songs != null) {
            sb.append(">").append(name).append("<\n");
            for (int i = 0; i < songs.size(); i++) {
                sb.append(songs.get(i));
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }
}
