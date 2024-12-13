package domain;

public class SessionManager {
	private static String loggedInDriverEmail;

	public static void login(String driverEmail) {
		loggedInDriverEmail = driverEmail;
	}

	public static void logout() {
		loggedInDriverEmail = null;
	}

	public static String getLoggedUser() {
		return loggedInDriverEmail;
	}

	public static boolean isLoggedIn() {
		return loggedInDriverEmail != null;
	}
}
