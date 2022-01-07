package letscode.challenge.moviesbattle.services;

import letscode.challenge.moviesbattle.data.UserRanking;
import letscode.challenge.moviesbattle.entities.Match;
import letscode.challenge.moviesbattle.entities.User;
import letscode.challenge.moviesbattle.repositories.MatchRepository;
import letscode.challenge.moviesbattle.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public RankingService(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    public void updateUserRanking(User user) {
        List<Match> listMatch = matchRepository.findMatchByUserAndStatus(user, Match.MatchStatus.ENDED);
        int rounds = 0;
        int hits = 0;
        for (Match match : listMatch) {
            rounds = rounds + match.getQuizList().size();
            hits = hits + match.getQuizList().size() - match.getErrors();
        }
        user.setRanking(((float)hits / (rounds == 0 ? 1 : rounds)));
        userRepository.save(user);
    }

    public List<UserRanking> getUserRanking(Short top) {
        Page<User> pageUser = userRepository.findAll(
                PageRequest.of(0, (top == null ? 10 : top),
                        Sort.by(Sort.Direction.DESC, "ranking")));

        List<UserRanking> listUserRanking = pageUser.stream().map(user -> {
            return new UserRanking(user.getRanking() == null ? 0 : (user.getRanking() * 100), user.getName());
        }).collect(Collectors.toList());

        return listUserRanking;
    }
}
