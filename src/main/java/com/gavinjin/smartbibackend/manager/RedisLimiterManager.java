package com.gavinjin.smartbibackend.manager;

import com.gavinjin.smartbibackend.util.common.ErrorCode;
import com.gavinjin.smartbibackend.util.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Provide Redis RateLimit Support
 */
@Service
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    /**
     * Rate limit
     * @param key Used to differentiate different RateLimiter
     */
    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);

        // Whenever an operation comes, try to take a token
        boolean canAcquire = rateLimiter.tryAcquire(1);
        if (!canAcquire) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }
}
