package session;

import model.TaiKhoan;

public final class Session {

	private static TaiKhoan currentUser;

	private Session() {
	}

	public static TaiKhoan getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(TaiKhoan taiKhoan) {
		currentUser = taiKhoan;
	}

	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	public static void clear() {
		currentUser = null;
	}

	public static boolean isAdmin() {
		return currentUser != null && currentUser.getRole() == TaiKhoan.Role.ADMIN;
	}

	public static boolean isKhachHang() {
		return currentUser != null && currentUser.getRole() == TaiKhoan.Role.KHACH_HANG;
	}

	public static boolean isDoiTac() {
		return currentUser != null && currentUser.getRole() == TaiKhoan.Role.DOI_TAC;
	}

	public static int getCurrentUserID() {

		if (currentUser == null) {
			throw new IllegalStateException("Chưa đăng nhập.");
		}

		return currentUser.getUserID();
	}

	public static TaiKhoan.Role getCurrentRole() {

		if (currentUser == null) {
			throw new IllegalStateException("Chưa đăng nhập.");
		}

		return currentUser.getRole();
	}

}