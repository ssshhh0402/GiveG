package com.dauction.domain.repository;

import com.dauction.domain.EthInfo;
import org.springframework.transaction.annotation.Transactional;

public interface IEthInfoRepository {
    EthInfo get(String ethUrl);

    @Transactional
    void put(String ethUrl, String blockNumber);
}
