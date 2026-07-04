package session;

import model.TaiKhoan;

/**
 * Lớp quản lý phiên đăng nhập hiện tại của ứng dụng (Singleton-style,
 * lưu trạng thái tĩnh). Chỉ lưu 1 người dùng đăng nhập tại một thời điểm.
 */
public final class Session {

	private static TaiKhoan currentUser;

	private Session() {
	}

	/** Lấy tài khoản đang đăng nhập, null nếu chưa đăng nhập. */
	public static TaiKhoan getCurrentUser() {
		return currentUser;
	}

	/** Thiết lập tài khoản hiện tại (dùng sau khi đăng nhập thành công). */
	public static void setCurrentUser(TaiKhoan taiKhoan) {
		currentUser = taiKhoan;
	}

	/** Kiểm tra đã có người dùng đăng nhập hay chưa. */
	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	/** Xóa phiên đăng nhập hiện tại (dùng khi đăng xuất). */
	public static void clear() {
		currentUser = null;
	}

	/** Kiểm tra người dùng hiện tại có vai trò đối tác hay không. */
	public static boolean isDoiTac() {
		return currentUser != null && currentUser.getRole() == TaiKhoan.Role.DOI_TAC;
	}

	/**
	 * Lấy userID của người dùng hiện tại.
	 *
	 * @return userID của người dùng đang đăng nhập.
	 * @throws IllegalStateException nếu chưa đăng nhập.
	 */
	public static int getCurrentUserID() {
		if (currentUser == null) {
			throw new IllegalStateException("Chưa đăng nhập.");
		}

		return currentUser.getUserID();
	}

	/**
	 * Lấy vai trò (role) của người dùng hiện tại.
	 *
	 * @return Role của người dùng đang đăng nhập.
	 * @throws IllegalStateException nếu chưa đăng nhập.
	 */
	public static TaiKhoan.Role getCurrentRole() {
		if (currentUser == null) {
			throw new IllegalStateException("Chưa đăng nhập.");
		}

		return currentUser.getRole();
	}
}