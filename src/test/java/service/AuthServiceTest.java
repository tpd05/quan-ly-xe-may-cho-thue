package service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import dao.TaiKhoanDAO;
import dto.TaiKhoanHienThiDTO;
import dto.TaiKhoanSuaDTO;
import model.TaiKhoan;
import session.Session;
import util.PasswordUtil;
import util.Result;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link AuthService}.
 *
 * Dùng constructor phụ {@code AuthService(TaiKhoanDAO)} với DAO trỏ vào
 * file CSV tạm ({@code @TempDir}), tránh đọc/ghi vào file dữ liệu thật.
 *
 * Vì {@code AuthService} phụ thuộc {@link Session} (biến static), bắt
 * buộc reset {@code Session.clear()} ở cả {@code @BeforeEach} và
 * {@code @AfterEach} để mỗi test độc lập, không rò trạng thái đăng nhập
 * sang test khác.
 *
 * LƯU Ý: enum {@code TaiKhoan.Role} hiện tại chỉ có 1 giá trị duy nhất
 * ({@code DOI_TAC}). Do đó nhánh kiểm tra "tài khoản đăng nhập đúng nhưng
 * không có quyền đối tác" trong {@code dangNhap()} hiện KHÔNG THỂ tạo dữ
 * liệu test hợp lệ để kích hoạt (vì không có role nào khác để gán) —
 * nhánh này sẽ được bổ sung test ngay khi hệ thống có thêm role thứ 2.
 */
class AuthServiceTest {

	@TempDir
	Path tempDir;

	private TaiKhoanDAO dao;
	private AuthService authService;

	private static final String USERNAME = "doitac1";
	private static final String PLAIN_PASSWORD = "password123";

	@BeforeEach
	void setUp() {
		Session.clear();
		String filePath = tempDir.resolve("taikhoan_test.csv").toString();
		dao = new TaiKhoanDAO(filePath);
		authService = new AuthService(dao);
	}

	@AfterEach
	void tearDown() {
		Session.clear();
	}

	/** Tạo và lưu 1 tài khoản đối tác với mật khẩu đã băm sẵn. */
	private TaiKhoan taoTaiKhoanDaHash(int userID, String username, String plainPassword) {
		TaiKhoan tk = new TaiKhoan(userID, username, PasswordUtil.hash(plainPassword), TaiKhoan.Role.DOI_TAC,
				"Nguyen Van A", "0912345678", "a@gmail.com", "123456789012", "Cua hang xe A", "123 Duong ABC");
		dao.insert(tk);
		return tk;
	}

	/** Tạo và lưu 1 tài khoản đối tác với mật khẩu CHƯA băm (dữ liệu cũ, plaintext). */
	private TaiKhoan taoTaiKhoanPlaintext(int userID, String username, String plainPassword) {
		TaiKhoan tk = new TaiKhoan(userID, username, plainPassword, TaiKhoan.Role.DOI_TAC,
				"Nguyen Van A", "0912345678", "a@gmail.com", "123456789012", "Cua hang xe A", "123 Duong ABC");
		dao.insert(tk);
		return tk;
	}

	@Nested
	@DisplayName("dangNhap()")
	class DangNhapTest {

		@Test
		@DisplayName("Đăng nhập thành công với tài khoản và mật khẩu đúng (đã hash)")
		void thanh_cong_voi_mat_khau_da_hash() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);

			TaiKhoan result = authService.dangNhap(USERNAME, PLAIN_PASSWORD);

			assertEquals(USERNAME, result.getUsername());
			assertTrue(Session.isLoggedIn());
			assertEquals(1, Session.getCurrentUserID());
		}

		@Test
		@DisplayName("Ném IllegalArgumentException khi username không tồn tại")
		void username_khong_ton_tai() {
			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					() -> authService.dangNhap("khong_ton_tai", PLAIN_PASSWORD));

			assertEquals("Sai tài khoản hoặc mật khẩu.", ex.getMessage());
			assertFalse(Session.isLoggedIn());
		}

		@Test
		@DisplayName("Ném IllegalArgumentException khi sai mật khẩu")
		void sai_mat_khau() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);

			IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
					() -> authService.dangNhap(USERNAME, "mat_khau_sai"));

			assertEquals("Sai tài khoản hoặc mật khẩu.", ex.getMessage());
			assertFalse(Session.isLoggedIn());
		}

		@Test
		@DisplayName("Đăng nhập thành công với mật khẩu cũ (plaintext), sau đó tự động migrate sang hash")
		void thanh_cong_voi_mat_khau_plaintext_va_tu_dong_migrate() {
			taoTaiKhoanPlaintext(1, "olduser", PLAIN_PASSWORD);

			authService.dangNhap("olduser", PLAIN_PASSWORD);

			assertTrue(Session.isLoggedIn());

			// Sau khi đăng nhập, mật khẩu trong DAO phải đã được băm lại (migrate)
			TaiKhoan afterLogin = dao.findByUsername("olduser");
			assertTrue(PasswordUtil.isHashed(afterLogin.getPassword()));
			assertTrue(PasswordUtil.matches(PLAIN_PASSWORD, afterLogin.getPassword()));
		}

		@Test
		@DisplayName("Không rò rỉ thông tin username tồn tại: cùng thông báo lỗi cho username sai và password sai")
		void thong_bao_loi_giong_nhau_cho_ca_2_truong_hop() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);

			IllegalArgumentException exUserSai = assertThrows(IllegalArgumentException.class,
					() -> authService.dangNhap("khong_ton_tai", PLAIN_PASSWORD));
			IllegalArgumentException exPassSai = assertThrows(IllegalArgumentException.class,
					() -> authService.dangNhap(USERNAME, "sai_mat_khau"));

			assertEquals(exUserSai.getMessage(), exPassSai.getMessage());
		}
	}

	@Nested
	@DisplayName("dangXuat()")
	class DangXuatTest {

		@Test
		@DisplayName("Đăng xuất xóa phiên đăng nhập hiện tại")
		void dang_xuat_xoa_session() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);
			authService.dangNhap(USERNAME, PLAIN_PASSWORD);
			assertTrue(Session.isLoggedIn());

			authService.dangXuat();

			assertFalse(Session.isLoggedIn());
		}
	}

	@Nested
	@DisplayName("requireDoiTac()")
	class RequireDoiTacTest {

		@Test
		@DisplayName("Không ném exception khi đã đăng nhập với vai trò đối tác")
		void khong_nem_exception_khi_da_dang_nhap() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);
			authService.dangNhap(USERNAME, PLAIN_PASSWORD);

			assertDoesNotThrow(() -> authService.requireDoiTac());
		}

		@Test
		@DisplayName("Ném IllegalStateException khi chưa đăng nhập")
		void nem_exception_khi_chua_dang_nhap() {
			assertThrows(IllegalStateException.class, () -> authService.requireDoiTac());
		}
	}

	@Nested
	@DisplayName("layThongTinTaiKhoan()")
	class LayThongTinTaiKhoanTest {

		@Test
		@DisplayName("Ném IllegalStateException khi chưa đăng nhập")
		void nem_exception_khi_chua_dang_nhap() {
			assertThrows(IllegalStateException.class, () -> authService.layThongTinTaiKhoan());
		}

		@Test
		@DisplayName("Trả về đúng thông tin, KHÔNG chứa userID và password")
		void tra_ve_dung_thong_tin_an_userID_va_password() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);
			authService.dangNhap(USERNAME, PLAIN_PASSWORD);

			TaiKhoanHienThiDTO dto = authService.layThongTinTaiKhoan();

			assertEquals(USERNAME, dto.getUsername());
			assertEquals("Nguyen Van A", dto.getHoTen());
			assertEquals("0912345678", dto.getSoDienThoai());
			assertEquals("a@gmail.com", dto.getEmail());
			assertEquals("123456789012", dto.getSoCCCD());
			assertEquals("Cua hang xe A", dto.getTenCuaHang());
			assertEquals("123 Duong ABC", dto.getDiaChiCuaHang());
			// DTO không có getUserID()/getPassword() -> không thể lộ dữ liệu này
		}
	}

	@Nested
	@DisplayName("suaThongTinTaiKhoan()")
	class SuaThongTinTaiKhoanTest {

		@BeforeEach
		void dangNhapTruoc() {
			taoTaiKhoanDaHash(1, USERNAME, PLAIN_PASSWORD);
			authService.dangNhap(USERNAME, PLAIN_PASSWORD);
		}

		private TaiKhoanSuaDTO taoInputHopLe() {
			return new TaiKhoanSuaDTO("Tran Thi B", "0987654321", "b@gmail.com", "987654321098",
					"Cua hang xe B", "456 Duong XYZ");
		}

		@Test
		@DisplayName("Ném IllegalStateException khi chưa đăng nhập")
		void nem_exception_khi_chua_dang_nhap() {
			Session.clear();

			assertThrows(IllegalStateException.class,
					() -> authService.suaThongTinTaiKhoan(taoInputHopLe()));
		}

		@Test
		@DisplayName("Cập nhật thành công với dữ liệu hợp lệ")
		void cap_nhat_thanh_cong() {
			
			Result result = authService.suaThongTinTaiKhoan(taoInputHopLe());

			assertTrue(result.isSuccess());

			TaiKhoan afterUpdate = dao.findByUsername(USERNAME);
			assertEquals("Tran Thi B", afterUpdate.getHoTen());
			assertEquals("0987654321", afterUpdate.getSoDienThoai());
			assertEquals("b@gmail.com", afterUpdate.getEmail());
			assertEquals("987654321098", afterUpdate.getSoCCCD());
			assertEquals("Cua hang xe B", afterUpdate.getTenCuaHang());
			assertEquals("456 Duong XYZ", afterUpdate.getDiaChiCuaHang());
		}

		@Test
		@DisplayName("Thất bại khi họ tên không hợp lệ")
		void that_bai_khi_ho_ten_khong_hop_le() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("A", "0987654321", "b@gmail.com", "987654321098",
					"Cua hang xe B", "456 Duong XYZ");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Họ tên không hợp lệ.", result.getMessage());
		}

		@Test
		@DisplayName("Thất bại khi số điện thoại không hợp lệ")
		void that_bai_khi_so_dien_thoai_khong_hop_le() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("Tran Thi B", "sai_sdt", "b@gmail.com", "987654321098",
					"Cua hang xe B", "456 Duong XYZ");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Số điện thoại không hợp lệ.", result.getMessage());
		}

		@Test
		@DisplayName("Thất bại khi email không hợp lệ")
		void that_bai_khi_email_khong_hop_le() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("Tran Thi B", "0987654321", "sai_email", "987654321098",
					"Cua hang xe B", "456 Duong XYZ");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Email không hợp lệ.", result.getMessage());
		}

		@Test
		@DisplayName("Thất bại khi số CCCD không hợp lệ")
		void that_bai_khi_cccd_khong_hop_le() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("Tran Thi B", "0987654321", "b@gmail.com", "123",
					"Cua hang xe B", "456 Duong XYZ");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Số CCCD không hợp lệ.", result.getMessage());
		}

		@Test
		@DisplayName("Thất bại khi tên cửa hàng rỗng")
		void that_bai_khi_ten_cua_hang_rong() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("Tran Thi B", "0987654321", "b@gmail.com", "987654321098",
					"", "456 Duong XYZ");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Tên cửa hàng không hợp lệ.", result.getMessage());
		}

		@Test
		@DisplayName("Thất bại khi địa chỉ cửa hàng rỗng")
		void that_bai_khi_dia_chi_cua_hang_rong() {
			TaiKhoanSuaDTO input = new TaiKhoanSuaDTO("Tran Thi B", "0987654321", "b@gmail.com", "987654321098",
					"Cua hang xe B", "");

			Result result = authService.suaThongTinTaiKhoan(input);

			assertFalse(result.isSuccess());
			assertEquals("Địa chỉ cửa hàng không hợp lệ.", result.getMessage());
		}
	}
}