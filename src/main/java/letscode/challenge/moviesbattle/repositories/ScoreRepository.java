package letscode.challenge.moviesbattle.repositories;

import letscode.challenge.moviesbattle.data.OmdbApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class ScoreRepository {

    @Value("${omdbapi.url}")
    private String omdbApiUrl;

    @Value("${omdbapi.apikey}")
    private String omdbApiApikey;

    public Float getScore(String imdbId) {
        String completeUrl = UriComponentsBuilder.fromHttpUrl(omdbApiUrl)
                .queryParam("apikey", omdbApiApikey)
                .queryParam("i", imdbId)
                .queryParam("plot", "short")
                .encode()
                .toUriString();

        OmdbApiResponse response = (new RestTemplate()).getForObject(completeUrl, OmdbApiResponse.class);
        if (response == null) {
            throw new RuntimeException("Error to get score from IMDB API");
        }
        return response.getImdbRating();
    }

}
