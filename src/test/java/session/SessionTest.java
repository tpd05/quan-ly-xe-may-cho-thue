package session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.TaiKhoan;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test cho {@link Session}.
 *
 * QUAN TRỌNG: Session lưu trạng thái bằng biến static (dùng chung cho toàn
 * bộ class, không phải theo từng instance). Nếu không reset trước/sau mỗi
 * test, kết quả của 1 test có thể ảnh hưởng đến test chạy sau đó (test
 * pollution). Do đó bắt buộc gọi {@code Session.clear()} ở cả
 * {@code @BeforeEach} và {@code @AfterEach} để đảm bảo mỗi test bắt đầu và
 * kết thúc ở trạng thái "chưa đăng nhập" độc lập với các test khác.
 */
class SessionTest {

	private TaiKhoan taiKhoanMau;

	@BeforeEach
	void setUp() {
		Session.clear();
		taiKhoanMau = new TaiKhoan(1, "doitac1", "password123", TaiKhoan.Role.DOI_TAC, "Nguyen Van A",
				"0912345678", "a@gmail.com", "123456789012", "Cua hang xe A", "123 Duong ABC");
	}

	@AfterEach
	void tearDown() {
		// Đảm bảo không rò trạng thái sang test/class khác chạy sau
		Session.clear();
	}

	@Test
	@DisplayName("Trạng thái ban đầu (chưa đăng nhập): isLoggedIn() = false, getCurrentUser() = null")
	void trang_thai_ban_dau_chua_dang_nhap() {
		assertFalse(Session.isLoggedIn());
		assertNull(Session.getCurrentUser());
	}

	@Test
	@DisplayName("setCurrentUser() -> isLoggedIn() = true và getCurrentUser() trả đúng tài khoản")
	void setCurrentUser_thanh_cong() {
		Session.setCurrentUser(taiKhoanMau);

		assertTrue(Session.isLoggedIn());
		assertEquals(taiKhoanMau, Session.getCurrentUser());
	}

	@Test
	@DisplayName("clear() reset về trạng thái chưa đăng nhập")
	void clear_reset_trang_thai() {
		Session.setCurrentUser(taiKhoanMau);
		assertTrue(Session.isLoggedIn());

		Session.clear();

		assertFalse(Session.isLoggedIn());
		assertNull(Session.getCurrentUser());
	}

	@Test
	@DisplayName("isDoiTac() = false khi chưa đăng nhập")
	void isDoiTac_false_khi_chua_dang_nhap() {
		assertFalse(Session.isDoiTac());
	}

	@Test
	@DisplayName("isDoiTac() = true khi tài khoản hiện tại có role DOI_TAC")
	void isDoiTac_true_khi_dang_nhap_doi_tac() {
		Session.setCurrentUser(taiKhoanMau);

		assertTrue(Session.isDoiTac());
	}

	@Test
	@DisplayName("getCurrentUserID() trả đúng userID khi đã đăng nhập")
	void getCurrentUserID_thanh_cong() {
		Session.setCurrentUser(taiKhoanMau);

		assertEquals(1, Session.getCurrentUserID());
	}

	@Test
	@DisplayName("getCurrentUserID() ném IllegalStateException khi chưa đăng nhập")
	void getCurrentUserID_ném_exception_khi_chua_dang_nhap() {
		assertThrows(IllegalStateException.class, Session::getCurrentUserID);
	}

	@Test
	@DisplayName("getCurrentRole() trả đúng role khi đã đăng nhập")
	void getCurrentRole_thanh_cong() {
		Session.setCurrentUser(taiKhoanMau);

		assertEquals(TaiKhoan.Role.DOI_TAC, Session.getCurrentRole());
	}

	@Test
	@DisplayName("getCurrentRole() ném IllegalStateException khi chưa đăng nhập")
	void getCurrentRole_nem_exception_khi_chua_dang_nhap() {
		assertThrows(IllegalStateException.class, Session::getCurrentRole);
	}

	@Test
	@DisplayName("setCurrentUser() với tài khoản mới sẽ thay thế tài khoản cũ (không cộng dồn)")
	void setCurrentUser_thay_the_tai_khoan_cu() {
		Session.setCurrentUser(taiKhoanMau);

		TaiKhoan taiKhoanKhac = new TaiKhoan(2, "doitac2", "password456", TaiKhoan.Role.DOI_TAC, "Tran Thi B",
				"0987654321", "b@gmail.com", "987654321098", "Cua hang xe B", "456 Duong XYZ");
		Session.setCurrentUser(taiKhoanKhac);

		assertEquals(2, Session.getCurrentUserID());
		assertEquals("doitac2", Session.getCurrentUser().getUsername());
	}
}