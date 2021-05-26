package hydata.model.test;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * 症状
 */
@NodeEntity(label = "Movie")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    private String tagline;
    /**
     * 症状名称
     */
    private String title;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", tagline='" + tagline + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
