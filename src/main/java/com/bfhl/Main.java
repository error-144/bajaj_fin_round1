package com.bfhl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Main {
    // === Hardcoded credentials (edit these, rebuild, then run) ===
    private static final String WEBHOOK = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjIyQkNFMjQ3NyIsIm5hbWUiOiJBcnlhbiBrdW1hciIsImVtYWlsIjoicy5hcnlhbi5rdW1hcjA0MEBnbWFpbC5jb20iLCJzdWIiOiJ3ZWJob29rLXVzZXIiLCJpYXQiOjE3NTY0NDgyNjMsImV4cCI6MTc1NjQ0OTE2M30.9bXkP8DjsJSoIo45wuZsD_lYsfjwiTK13NQ4eKbVCyY"; // <-- paste full JWT

    // === Embedded SQL Query ===
    private static final String SQL_QUERY = "SELECT p.amount AS salary, CONCAT(e.first_name, ' ', e.last_name) AS name, TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS age, d.department_name FROM payments p JOIN employee e ON e.emp_id = p.emp_id JOIN department d ON d.department_id = e.department WHERE DAY(p.payment_time) <> 1 ORDER BY p.amount DESC LIMIT 1";

    private static String jsonEscape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int)c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int postJson(String urlStr, String token, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", token);
        conn.setDoOutput(true);
        byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }
        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        if (is != null) {
            String resp = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println("Response: " + resp);
            is.close();
        }
        conn.disconnect();
        return code;
    }

    public static void main(String[] args) throws Exception {
        String sql = SQL_QUERY;
        String json = "{\"finalQuery\":\""+ jsonEscape(sql) +"\"}";

        System.out.println("Prepared JSON: " + json);
        System.out.println("Webhook: " + WEBHOOK);
        System.out.println("Token prefix: " + (ACCESS_TOKEN.length() > 10 ? ACCESS_TOKEN.substring(0,10) + "..." : "(empty)"));

        if (ACCESS_TOKEN.contains("PASTE_YOUR_FULL_JWT_TOKEN_HERE")) {
            System.err.println("ERROR: You must paste your full JWT into Main.java before building.");
            System.exit(2);
        }

        int code = postJson(WEBHOOK, ACCESS_TOKEN, json);
        System.out.println("HTTP Status: " + code);
        if (code < 200 || code >= 300) System.exit(1);
    }
} 