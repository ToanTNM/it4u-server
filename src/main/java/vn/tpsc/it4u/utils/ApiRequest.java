package vn.tpsc.it4u.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.springframework.beans.factory.annotation.Value;

// import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
// import com.google.auth.oauth2.AccessToken;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Component;

@Component
public class ApiRequest {

	public String getRequestApi(String urlIt4u, String infoApi, String csrfToken, String unifises) {
		try {
			URL url = new URL(urlIt4u + infoApi);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Cookie", "csrf_token=" + csrfToken + "; unifises=" + unifises);
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			StringBuilder build = new StringBuilder();
			if (inputStream != null) {
				InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(ISreader);
				String line = reader.readLine();
				while (line != null) {
					build.append(line);
					line = reader.readLine();
				}
			}
			String jsonString = build.toString();
			return jsonString;

		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String postRequestApi(String urlIt4u, String infoApi, String csrfToken, String unifises, String dataPost) {
		try {
			String url = urlIt4u + infoApi;
			StringBuffer response = new StringBuffer();
			URL UrlObj = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-Csrf-Token", csrfToken);
			connection.setRequestProperty("Cookie", "csrf_token=" + csrfToken + "; unifises=" + unifises);
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			// String urlPostParameters = "start=1577379600000&end=1577552400000";
			byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
			outputStream.write(out);
			outputStream.flush();
			outputStream.close();

			System.out.println("Send 'HTTP POST' request to : " + url);

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = inputReader.readLine()) != null) {
					response.append(inputLine);
				}
				inputReader.close();
			}
			return response.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String putRequestApi(String urlIt4u, String infoApi, String csrfToken, String unifises, String dataPost) {
		try {
			String url = urlIt4u + infoApi;
			StringBuffer response = new StringBuffer();
			URL UrlObj = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("X-Csrf-Token", csrfToken);
			connection.setRequestProperty("Cookie", "csrf_token=" + csrfToken + "; unifises=" + unifises);
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			// String urlPostParameters = "start=1577379600000&end=1577552400000";
			byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
			outputStream.write(out);
			outputStream.flush();
			outputStream.close();

			System.out.println("Send 'HTTP POST' request to : " + url);

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = inputReader.readLine()) != null) {
					response.append(inputLine);
				}
				inputReader.close();
			}
			return response.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String getRequestDev(String urlIt4u, String infoApi) {
		try {
			HttpsURLConnection conn = null;
			URL url = new URL(urlIt4u + infoApi);
			conn = (HttpsURLConnection) url.openConnection();
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			StringBuilder build = new StringBuilder();
			if (inputStream != null) {
				InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(ISreader);
				String line = reader.readLine();
				while (line != null) {
					build.append(line);
					line = reader.readLine();
				}
			}
			String jsonString = build.toString();
			return jsonString;

		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String postRequestIt4u(String urlIt4u, String infoApi, String dataPost) {
		try {
			String url = urlIt4u + infoApi;
			StringBuffer response = new StringBuffer();
			URL UrlObj = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			// String urlPostParameters = "start=1577379600000&end=1577552400000";
			byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
			outputStream.write(out);
			outputStream.flush();
			outputStream.close();

			System.out.println("Send 'HTTP POST' request to : " + url);

			int responseCode = connection.getResponseCode();
			Map<String, List<String>> heads = connection.getHeaderFields();
			List<String> cookie = heads.get("Set-Cookie");
			// String getCookie = connection.get
			System.out.println("Response Code : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = inputReader.readLine()) != null) {
					response.append(inputLine);
				}
				inputReader.close();
			}
			return cookie.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String postLoginZabbix(String urlIt4u, String infoApi, String dataPost) {
		try {
			String url = urlIt4u + infoApi;
			StringBuffer response = new StringBuffer();
			URL UrlObj = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
			outputStream.write(out);
			outputStream.flush();
			outputStream.close();

			System.out.println("Send 'HTTP POST' request to : " + url);

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = inputReader.readLine()) != null) {
					response.append(inputLine);
				}
				inputReader.close();
			}
			return response.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String getTokenUcrm(String urlUcrm) {
		try {
			HttpsURLConnection conn = null;
			URL url = new URL(urlUcrm);
			conn = (HttpsURLConnection) url.openConnection();
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			StringBuilder build = new StringBuilder();
			if (inputStream != null) {
				InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(ISreader);
				String line = reader.readLine();
				while (line != null) {
					build.append(line);
					line = reader.readLine();
				}
			}
			String jsonString = build.toString();
			return jsonString;
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String getCookieUcrm(String urlUcrm, String dataPost) {
		try {
			URL UrlObj = new URL(urlUcrm);

			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
			outputStream.write(out);
			outputStream.flush();
			outputStream.close();

			System.out.println("Send 'HTTP POST' request to : " + urlUcrm);
			Map<String, List<String>> heads = connection.getHeaderFields();
			List<String> cookie = heads.get("set-cookie");
			return cookie.toString();
		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String getRequestUCRM(String urlUcrm, String auth) {
		try {
			URL url = new URL(urlUcrm);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("X-Auth-App-Key", auth);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			StringBuilder build = new StringBuilder();
			if (inputStream != null) {
				InputStreamReader ISreader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
				BufferedReader reader = new BufferedReader(ISreader);
				String line = reader.readLine();
				while (line != null) {
					build.append(line);
					line = reader.readLine();
				}
			}
			String jsonString = build.toString();
			return jsonString;

		} catch (Exception e) {
			System.out.println("===============ERROR===============\n" + e.getMessage() + "\n\n\n");
			return e.getMessage();
		}
	}

	public String getConnectionFirebase(String urlFirebase, String senderId, String getAccessToken, String dataPost)
			throws IOException {
		URL url = new URL(urlFirebase);
		StringBuffer response = new StringBuffer();
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "key=" + getAccessToken);
		httpURLConnection.setRequestProperty("Sender", "id=" + senderId);
		httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
		byte[] out = dataPost.getBytes(StandardCharsets.UTF_8);
		outputStream.write(out);
		outputStream.flush();
		outputStream.close();
		System.out.println("Send 'HTTP POST' request to : " + url);
		int responseCode = httpURLConnection.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String inputLine;
			while ((inputLine = inputReader.readLine()) != null) {
				response.append(inputLine);
			}
			inputReader.close();
		}
		return response.toString();
	}

	// public static String getAccessToken() throws IOException {
	// String path = ".//serviceAccountKey.json";
	// GoogleCredential googleCredential = GoogleCredential.fromStream(new
	// FileInputStream(path))
	// .createScoped(Arrays.asList(SCOPES));
	// googleCredential.refreshToken();
	// return googleCredential.getAccessToken();
	// }
}