package com.fiora.note2.service.impl;

import com.fiora.note2.dao.TokenRepository;
import com.fiora.note2.model.Token;
import com.fiora.note2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;
    @Override
    public Token findAliyunToken() {
        return tokenRepository.findById(1L).get();
    }

    @Override
    public Token findPrivateToken() {
        return tokenRepository.findById(2L).get();
    }
}
