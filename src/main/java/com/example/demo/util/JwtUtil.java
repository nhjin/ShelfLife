package com.example.demo.util;

import com.example.demo.dto.AuthVo;
import io.jsonwebtoken.*;

import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static String SECRET_KEY = "SKEY";    // 암호화하기 위한 키

    private static long tokenValidMilisecond = 1000L * 60;  // JWT 만료 시간 1분(access token)
    //private static long tokenRefreshValidMilisecond = 1000L * 60 * 60 * 24 * 365;  // JWT 만료 시간 1년(refresh token)
    private static long tokenRefreshValidMilisecond = 1000L * 60 * 60;

    private static String CODE_HEADER = "4K8";   // 직렬화된 코드에 보안을 위해 임의적인 문자나 숫자를 추가

    // 토큰 생성 함수
    public static String createToken(String id, String key, String tokenType) {
        // Auth 인스턴스 생성
        AuthVo authvo = new AuthVo();
        authvo.setId(id);
        authvo.setKey(key);
        authvo.setTokenType(tokenType);

        // JwtUtil 인스턴스 생성
        JwtUtil jwt = new JwtUtil();

        // Auth 인스턴스를 직렬화한다.
        String code = jwt.convertSerializable(authvo);

        // 직렬화된 코드에 보안을 위해 임의적인 문자나 숫자를 추가한다. 4K8 코드를 추가한다.
        code = CODE_HEADER + code;

        // JWT 토큰 생성
        String token = jwt.createToken(code, tokenType);
        return token;
    }


    // 토큰 생성 함수 
    public String createToken(String code, String tokenType) {
        // Claims을 생성
        Claims claims = Jwts.claims().setId(code);

        // token 유형에 따라 만료시간 설정
        long exptime    =  tokenValidMilisecond;
        if(tokenType.equals("RT"))  exptime    =  tokenRefreshValidMilisecond;

        Date now = new Date();
        // JWT 토큰을 만드는데, Payload 정보와 생성시간, 만료시간, 알고리즘 종료와 암호화 키를 넣어 암호화 함.
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exptime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 직렬화 함수
    public String convertSerializable(AuthVo authvo) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(authvo);
                // 직렬화 코드  
                byte[] data = baos.toByteArray();
                // 직렬화된 것은 Base64로 암호화 
                return Base64.getEncoder().encodeToString(data);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    // 역직렬화 함수
    public static AuthVo convertData(String code) {
        // Base64 복호화
        byte[] data = Base64.getDecoder().decode(code);
        // 역직렬화   
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object objectMember = ois.readObject();
                // Auth 인스턴스로 캐스팅
                return (AuthVo) objectMember;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    // Auth 정보를 리턴한다.
    public static AuthVo getAuth(String token) {
        // JWT 토큰 복호화
        AuthVo authvo = new AuthVo();
        try {
            Jws<Claims> claims = JwtUtil.getClaims(token);

            // JWT 토큰 검증
            if (claims != null && JwtUtil.validateToken(claims)) {
                // id를 취득한다.
                String id = JwtUtil.getKey(claims);

                // code head 코드 제거
                id = id.substring(CODE_HEADER.length());

                // 역직렬화
                authvo = JwtUtil.convertData(id);
                authvo.setTokenValidate("Y");
            } else {
                // 토큰이 정합성이 맞지 않으면
                authvo.setTokenValidate("N");
                //System.out.println("error");
            }
        }catch(Exception e){
            authvo.setTokenValidate("N");
            System.out.println("[getAuth error]" + e.getMessage());
        }
        return authvo;
    }


    // String으로 된 코드를 복호화한다.
    public static Jws<Claims> getClaims(String token) {
        try {
            // 암호화 키로 복호화한다.
            // 즉 암호화 키가 다르면 에러가 발생한다.
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
        } catch (SignatureException e) {
            // System.out.println("SignatureException: " + e);
            return null;
        } catch (Exception e) {
            //	 System.out.println("Exception: " + e);
            return null;
        }
    }

    // 토큰 검증 함수
    public static boolean validateToken(Jws<Claims> claims) {
        // 토큰 만료 시간이 현재 시간을 지났는지 검증
        return !claims.getBody()
                .getExpiration()
                .before(new Date());
    }

    // 토큰을 통해 Payload의 ID를 취득
    public static String getKey(Jws<Claims> claims) {
        // Id 취득
        return claims.getBody().getId();
    }

    // 토큰을 통해 Payload의 데이터를 취득
    public static Object getClaims(Jws<Claims> claims, String key) {
        // 데이터 취득
        return claims.getBody().get(key);
    }

    /**
     * 토큰 검증
     * @param jwt
     * @param APISecret
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> verifyJWT(String jwt, String APISecret) throws UnsupportedEncodingException {
		Map<String, Object> claimMap = null;
		try {
			Claims claims = Jwts.parser().setSigningKey(APISecret.getBytes("UTF-8")) // Set
					.parseClaimsJws(jwt) // 파싱 및 검증, 실패 시 에러
					.getBody();

			claimMap = claims;

			// Date expiration = claims.get("exp", Date.class);
			// String data = claims.get("data", String.class);

		} catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
			System.out.println(e);
		} catch (Exception e) { // 그외 에러났을 경우
			System.out.println(e);
		}
		return claimMap;
	}

}