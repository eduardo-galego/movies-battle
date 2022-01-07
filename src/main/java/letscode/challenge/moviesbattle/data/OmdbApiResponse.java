package letscode.challenge.moviesbattle.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbApiResponse {

    private String imdbID;
    private String title;
    private Float imdbRating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Float imdbRating) {
        this.imdbRating = imdbRating;
    }
}
