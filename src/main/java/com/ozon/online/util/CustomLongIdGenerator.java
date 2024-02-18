package com.ozon.online.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class CustomLongIdGenerator implements IdentifierGenerator {
    @Override
    public Long generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        Random random = new Random();
        int q = random.nextInt(19) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());
    }
}
