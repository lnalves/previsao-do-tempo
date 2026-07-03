package model;

import dal.DAO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class OpenMeteoService {

    public static String tempoAPI(double latitude, double longitude) throws Exception {
        DAO dao = new DAO();
        String baseUrl = dao.getConfiguracao("API_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new RuntimeException("URL base da API não encontrada nas configurações do banco de dados.");
        }

        String parametros = String.format(Locale.US, "?latitude=%.4f&longitude=%.4f&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&hourly=temperature_2m,precipitation_probability,apparent_temperature&current=temperature_2m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America/Sao_Paulo", latitude, longitude);
        String apiUrl = baseUrl + parametros;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("Código de Resposta: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            throw new RuntimeException("Falha na requisição HTTP: " + responseCode + ". Resposta da API: " + errorResponse.toString());
        }
    }
}