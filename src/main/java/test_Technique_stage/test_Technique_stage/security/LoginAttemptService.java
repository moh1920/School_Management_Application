package test_Technique_stage.test_Technique_stage.security;


import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private final long BLOCK_TIME = TimeUnit.MINUTES.toMillis(1);

    private ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Long> blockCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        blockCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);

        if (attempts >= MAX_ATTEMPT) {
            blockCache.put(key, System.currentTimeMillis() + BLOCK_TIME);
        }
    }

    public boolean isBlocked(String key) {
        Long blockTime = blockCache.get(key);
        if (blockTime == null) return false;

        if (System.currentTimeMillis() > blockTime) {
            blockCache.remove(key);
            attemptsCache.remove(key);
            return false;
        }

        return true;
    }
}

