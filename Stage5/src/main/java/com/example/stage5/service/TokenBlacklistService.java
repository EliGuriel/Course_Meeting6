package com.example.stage5.service;


import com.example.stage5.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 +--------------------------------------------------------------+
 |                  TokenBlacklistService                       |
 +--------------------------------------------------------------+
 | @Service                                                     |
 | @RequiredArgsConstructor                                     |
 |                                                              |
 | Fields:                                                      |
 | - JwtUtil jwtUtil                                            |
 | - ConcurrentHashMap<String, Instant> blacklist               |
 +--------------------------------------------------------------+
 |
 v
 +--------------------------------------------------------------+
 | addToBlacklist(String token)                                 |
 |                                                              |
 | - Extract expiration time from the token using jwtUtil       |
 |                                                              |
 | - Add the token and its expiration time to the blacklist     |
 |                                                              |
 | - Print message: "Token added to blacklist: [token]"         |
 |                                                              |
 | - Call removeExpiredTokens() to clean up expired tokens      |
 +--------------------------------------------------------------+
 |
 v
 +--------------------------------------------------------------+
 | removeExpiredTokens()                                        |
 |                                                              |
 | - Iterate over blacklist entries                             |
 | - Remove tokens that have expired (current time is after     |
 |   expiration time)                                           |
 +--------------------------------------------------------------+
 |
 v
 +--------------------------------------------------------------+
 | isBlacklisted(String token)                                  |
 |                                                              |
 | - Retrieve the token's expiration time from the blacklist    |
 |                                                              |
 | - If the token is not found, return false                    |
 |                                                              |
 | - If the token has expired, remove it from the blacklist     |
 |   and return false                                           |
 |                                                              |
 | - If the token is still valid, return true                   |
 +--------------------------------------------------------------+

 Add to Blacklist:

 The addToBlacklist method adds a JWT token to the blocklist.
 It first extracts the token’s expiration time using jwtUtil.extractExpiration.
 The token and its expiration time are stored in the ConcurrentHashMap called blocklist.
 A message is printed to indicate that the token has been added to the blocklist.
 The removeExpiredTokens method is called to clean up any expired tokens from the blocklist.
 Remove Expired Tokens:

 The removeExpiredTokens method iterates over the entries in the blocklist.
 It removes any tokens that have expired, ensuring that the blocklist only contains valid tokens.
 Check if the Token is Blocklisted:

 The isBlacklisted method checks whether a given token is in the blocklist.
 It retrieves the token’s expiration time from the blocklist.
 If the token is not found, it returns false.
 If the token is found but has expired, it is removed from the blocklist, and false is returned.
 If the token is found and still valid, true is returned.
 Summary:
 This flowchart provides an overview of how the TokenBlacklistService class manages the blocklist of JWT tokens.
 It shows how tokens are added to the blocklist, how expired tokens are removed, and how the system checks if
 a token is currently blocklisted. The use of a ConcurrentHashMap ensures thread-safe operations,
 making it suitable for concurrent environments.
 This service is crucial for maintaining security by preventing the reuse of invalid or compromised tokens.
 */

// TODO stage4 - 1. Implement the TokenBlacklistService class.

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final JwtUtil jwtUtil;

    private final ConcurrentHashMap<String, Instant> blacklist = new ConcurrentHashMap<>();

    public void addToBlacklist(String token) {
        Instant expirationTime = jwtUtil.extractExpiration(token).toInstant();
        blacklist.put(token, expirationTime);
        System.out.println("Token added to blacklist: " + token);
        removeExpiredTokens();
    }

    private void removeExpiredTokens() {
        // iterate over the entries in the blacklist
        Iterator<ConcurrentHashMap.Entry<String, Instant>> iterator = blacklist.entrySet().iterator();
        while (iterator.hasNext()) {
            ConcurrentHashMap.Entry<String, Instant> entry = iterator.next();
            System.out.println("Token: " + entry.getKey() + " Expiration: " + entry.getValue());
            // remove tokens that have expired
            if (Instant.now().isAfter(entry.getValue())) {
                iterator.remove();
                System.out.println("Token removed from blacklist: " + entry.getKey());
            }
        }
    }

    public boolean isBlacklisted(String token) {
        Instant expiration = blacklist.get(token);
        if (expiration == null) {
            return false;
        }
        if (Instant.now().isAfter(expiration)) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

}
