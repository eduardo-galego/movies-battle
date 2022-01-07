package letscode.challenge.moviesbattle.controllers;

import letscode.challenge.moviesbattle.data.UserRanking;
import letscode.challenge.moviesbattle.services.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/ranking")
public class UserController {

    private final RankingService rankingService;

    public UserController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public List<UserRanking> listAll(@RequestParam Short top) {
        return rankingService.getUserRanking(top);
    }

}
