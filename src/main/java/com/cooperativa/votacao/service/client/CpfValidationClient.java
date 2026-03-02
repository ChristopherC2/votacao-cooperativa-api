package com.cooperativa.votacao.service.client;

import com.cooperativa.votacao.dto.CpfValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CpfValidationClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isAbleToVote(String cpf) {
//        String url = "https://user-info.herokuapp.com/users/" + cpf;
//
//        ResponseEntity<CpfValidationResponse> response =
//                restTemplate.getForEntity(url, CpfValidationResponse.class);
//
//        return "ABLE_TO_VOTE".equals(response.getBody().getStatus());
        return true;
    }
}
