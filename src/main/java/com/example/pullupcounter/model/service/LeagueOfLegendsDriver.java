package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.Game;
import com.example.pullupcounter.data.entity.InGameAccount;
import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.data.repository.GameRepository;
import com.example.pullupcounter.data.repository.InGameAccountRepository;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.GameName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class LeagueOfLegendsDriver implements GameDriver {
    private final static String GET_MATCHES_URI =
            "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/{puuid}/ids?start=0&count=10&api_key={api_key}";
    private final static String GET_MATCH_URI =
            "https://europe.api.riotgames.com/lol/match/v5/matches/{match_id}?api_key={api_key}";
    private final static String GET_ACCOUNT_URI =
            "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/{gameName}/EUW?api_key={api_key}";

    @Autowired
    InGameAccountRepository accountRepo;

    @Autowired
    GameRepository gameRepo;

    @Override
    public Integer getDeaths(InGameAccount account) throws ApiAccessException {
        try {
            List<String> matches = getMatches(account);
            int s = 0;
            for (String matchId : matches) {
                JsonNode matchRootNode = getMatch(account, matchId);
                if (matchRootNode.get("metadata").get("matchId").asText().equals(account.getMark()))
                    break;
                for (JsonNode participantNode : matchRootNode.get("info").get("participants")) {
                    if (participantNode.get("puuid").asText().equals(account.getPuuid())) {
                        s += participantNode.get("deaths").asInt();
                    }
                }
            }
            updateInGameAccount(account, matches);
            return s;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.FORBIDDEN) || errorException.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
                throw new ApiAccessException(errorException, getName().name());
            else throw errorException;
        }
    }

    private void updateInGameAccount(InGameAccount account, List<String> matches) throws JsonProcessingException {
        if (matches.size() > 0) {
            JsonNode rootNode = getMatch(account, matches.get(0));
            account.setDay(Timestamp.from(Instant.ofEpochMilli(rootNode.get("info").get("gameStartTimestamp").asLong())));
            account.setMark(matches.get(0));
            accountRepo.save(account);
        }
    }


    @Override
    public GameName getName() {
        return GameName.LeagueOfLegends;
    }

    @Override
    public InGameAccount bindAccount(String inGameAccountName, User user) throws ApiAccessException {
        try {
            Game game = gameRepo.getGameByName(getName().name());
            String apiKey = game.getKey();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(GET_ACCOUNT_URI,
                    String.class,
                    inGameAccountName,
                    apiKey
            );
            if (!response.getStatusCode().equals(HttpStatus.OK))
                return null;
            String jsonString = response.getBody();
            System.out.println(jsonString);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode accountRootNode = mapper.readValue(jsonString, new TypeReference<>() {
            });
            InGameAccount inGameAccount = new InGameAccount();
            inGameAccount.setGame(game);
            inGameAccount.setPuuid(accountRootNode.get("puuid").asText());
            inGameAccount.setInGameAccountName(inGameAccountName);
            inGameAccount.setUser(user);
            accountRepo.save(inGameAccount);
            return inGameAccount;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.FORBIDDEN) || errorException.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
                throw new ApiAccessException(errorException, getName().name());
            else if (errorException.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return null;
            else throw errorException;
        }
    }

    @Override
    public boolean validateKey(String key) {
        if (key == null || key.isEmpty())
            return false;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(GET_ACCOUNT_URI,
                    String.class,
                    "Rapirys",
                    key
            );
            return true;
        } catch (HttpClientErrorException errorException) {
            System.out.println(errorException.getStatusCode());
            return errorException.getStatusCode().is2xxSuccessful();
        }
    }

    private List<String> getMatches(InGameAccount account) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String jsonString = restTemplate.getForObject(GET_MATCHES_URI,
                String.class,
                account.getPuuid(),
                account.getGame().getKey()
        );
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<>() {
        });

    }

    private JsonNode getMatch(InGameAccount account, String matchId) throws JsonProcessingException {
        WebClient webClient = WebClient.create();
        String jsonString = webClient.get()
                .uri(GET_MATCH_URI, matchId, account.getGame().getKey())
                .retrieve()
                .bodyToMono(String.class)
                .block();


//        RestTemplate restTemplate = new RestTemplate();
//
//        String jsonString = restTemplate.getForObject(GET_MATCH_URI,
//                String.class,
//                matchId,
//                account.getGame().getKey()
//        );
        System.out.println(jsonString);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.readTree(jsonString));
        return mapper.readTree(jsonString);
    }

}
