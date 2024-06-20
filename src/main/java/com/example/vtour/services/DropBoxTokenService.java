package com.example.vtour.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
@Service
public class DropBoxTokenService {
    public String getAccessToken(String code) {
        String appKey = "uhqcpb2jg2bpzaw";
        String appSecret = "epejqvhopew04yf";
        try {
            URL url = new URL("https://api.dropboxapi.com/oauth2/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String parameters = "code=" + code + "&grant_type=authorization_code&client_id=" + appKey +
                    "&client_secret=" + appSecret + "&redirect_uri=https://google.com";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = parameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            String accessToken = rootNode.get("access_token").asText();
            return accessToken;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "ERROR";
        }
    }

}
