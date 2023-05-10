package com.example.pullupcounter.model.service;

import com.example.pullupcounter.data.entity.InGameAccount;
import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.GameName;

public interface GameDriver {
    public Integer getDeaths(InGameAccount account) throws ApiAccessException;

    public GameName getName();

    public InGameAccount bindAccount(String inGameAccountName, User user) throws ApiAccessException;

    public boolean validateKey(String key);

}
