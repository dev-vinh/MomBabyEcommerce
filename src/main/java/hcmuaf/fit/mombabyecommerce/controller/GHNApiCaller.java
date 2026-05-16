package hcmuaf.fit.mombabyecommerce.controller;

import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class GHNApiCaller {
    private final String shopId = ConfigLoader.get("ghn.shop_id");
    private final String token = ConfigLoader.get("ghn.token");
    private final String baseUrl = ConfigLoader.get("ghn.url");

    private final String createOrderUrl ="shipping-order/create";
    private final String cancelOrderUrl ="switch-status/cancel";

    public  String createOrder(String payload) throws IOException {
        URI uri = URI.create(baseUrl + createOrderUrl);
        HttpsURLConnection conn = (HttpsURLConnection) uri.toURL().openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ShopId", shopId);
        conn.setRequestProperty("Token", token);

        String curl = buildCurlCommand(uri.toString(), payload);
        System.out.println("==> CURL Preview:\n" + curl + "\n");


        try( OutputStream os = conn.getOutputStream()){
            byte[] data = payload.getBytes(StandardCharsets.UTF_8);
            os.write(data);
        }
        return getResponse(conn);

    }

    public  String cancelOrder(String payload) throws IOException {
        URI uri = URI.create(baseUrl + cancelOrderUrl);
        HttpsURLConnection conn = (HttpsURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("ShopId", shopId);
        conn.setRequestProperty("Token", token);

        String curl = buildCurlCommand(uri.toString(), payload);
        System.out.println("==> CURL Preview:\n" + curl + "\n");

        try( OutputStream os = conn.getOutputStream()){
            byte[] data = payload.getBytes(StandardCharsets.UTF_8);
            os.write(data);
        }

        return getResponse(conn);

    }
    private String getResponse(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        InputStream responseStream = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        return response.toString();
    }

    private String buildCurlCommand(String url, String payload) {
        StringBuilder sb = new StringBuilder("curl --location --request POST '");
        sb.append(url).append("' \\\n");

        sb.append("--header 'Content-Type: application/json' \\\n");
        sb.append("--header 'ShopId: ").append(shopId).append("' \\\n");
        sb.append("--header 'Token: ").append(token).append("' \\\n");

        String compactPayload = payload.replace("\"", "\\\"").replaceAll("\n", "").replaceAll("\\s{2,}", " ");
        sb.append("--data-raw \"").append(compactPayload).append("\"");

        return sb.toString();
    }
}
