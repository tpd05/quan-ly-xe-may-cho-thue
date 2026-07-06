package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import model.TaiKhoan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link TaiKhoanDAO}.
 *
 * Dùng constructor phụ {@code TaiKhoanDAO(String filePath)} kèm {@code @TempDir}
 * để trỏ vào file CSV tạm, tránh đọc/ghi vào file dữ liệu thật.
 */
class TaiKhoanDAOTest {

	@TempDir
	Path tempDir;

	private TaiKhoanDAO dao;

	@BeforeEach
	void setUp() {
		String filePath = tempDir.resolve("taikhoan_test.csv").toString();
		dao = new TaiKhoanDAO(filePath);
	}

	private TaiKhoan taoTaiKhoanMau(int userID, String username, String password) {
		return new TaiKhoan(userID, username, password, TaiKhoan.Role.DOI_TAC, "Nguyen Van A", "0912345678",
				"a@gmail.com", "123456789012", "Cua hang xe A", "123 Duong ABC");
	}

	@Test
	@DisplayName("findAll() trả về danh sách rỗng khi file mới khởi tạo")
	void findAll_rong_khi_moi_khoi_tao() {
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("insert() thêm tài khoản thành công, findAll() thấy đúng dữ liệu")
	void insert_va_findAll() {
		TaiKhoan tk = taoTaiKhoanMau(1, "doitac1", "password123");

		assertTrue(dao.insert(tk));

		TaiKhoan saved = dao.findById(1);
		assertEquals("doitac1", saved.getUsername());
		assertEquals("password123", saved.getPassword());
		assertEquals(TaiKhoan.Role.DOI_TAC, saved.getRole());
		assertEquals("Nguyen Van A", saved.getHoTen());
		assertEquals("0912345678", saved.getSoDienThoai());
		assertEquals("a@gmail.com", saved.getEmail());
		assertEquals("123456789012", saved.getSoCCCD());
		assertEquals("Cua hang xe A", saved.getTenCuaHang());
		assertEquals("123 Duong ABC", saved.getDiaChiCuaHang());
	}

	@Test
	@DisplayName("findById() trả về null khi không tồn tại")
	void findById_khong_tim_thay() {
		assertNull(dao.findById(999));
	}

	@Test
	@DisplayName("findByUsername() tìm đúng tài khoản theo username")
	void findByUsername_tim_thay() {
		dao.insert(taoTaiKhoanMau(1, "doitac1", "password123"));
		dao.insert(taoTaiKhoanMau(2, "doitac2", "password456"));

		TaiKhoan found = dao.findByUsername("doitac2");

		assertEquals(2, found.getUserID());
		assertEquals("doitac2", found.getUsername());
	}

	@Test
	@DisplayName("findByUsername() trả về null khi username không tồn tại")
	void findByUsername_khong_tim_thay() {
		dao.insert(taoTaiKhoanMau(1, "doitac1", "password123"));

		assertNull(dao.findByUsername("khong_ton_tai"));
	}

	@Test
	@DisplayName("update() cập nhật thông tin tài khoản thành công")
	void update_thanh_cong() {
		dao.insert(taoTaiKhoanMau(1, "doitac1", "password123"));

		TaiKhoan tkMoi = taoTaiKhoanMau(1, "doitac1", "matkhaumoi123");
		boolean result = dao.update(tkMoi);

		assertTrue(result);
		assertEquals("matkhaumoi123", dao.findById(1).getPassword());
	}

	@Test
	@DisplayName("update() trả về false khi ID không tồn tại")
	void update_khong_ton_tai() {
		dao.insert(taoTaiKhoanMau(1, "doitac1", "password123"));

		boolean result = dao.update(taoTaiKhoanMau(999, "khac", "matkhau123"));

		assertFalse(result);
	}

	@Test
	@DisplayName("delete() xóa tài khoản thành công")
	void delete_thanh_cong() {
		dao.insert(taoTaiKhoanMau(1, "doitac1", "password123"));

		assertTrue(dao.delete(1));
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("parse() ném IllegalStateException khi thiếu cột dữ liệu")
	void parse_du_lieu_khong_hop_le() {
		assertThrows(IllegalStateException.class, () -> dao.parse("1,doitac1,password123"));
	}

	@Test
	@DisplayName("format() trả về đúng chuỗi CSV theo đúng thứ tự cột")
	void format_dung_dinh_dang() {
		TaiKhoan tk = taoTaiKhoanMau(1, "doitac1", "password123");

		String line = dao.format(tk);

		assertEquals("1,doitac1,password123,DOI_TAC,Nguyen Van A,0912345678,a@gmail.com,"
				+ "123456789012,Cua hang xe A,123 Duong ABC", line);
	}

	@Test
	@DisplayName("Toàn vẹn dữ liệu qua chu kỳ format -> parse (round-trip)")
	void round_trip_format_parse() {
		TaiKhoan original = taoTaiKhoanMau(1, "doitac1", "password123");

		TaiKhoan parsed = dao.parse(dao.format(original));

		assertEquals(original.getUserID(), parsed.getUserID());
		assertEquals(original.getUsername(), parsed.getUsername());
		assertEquals(original.getPassword(), parsed.getPassword());
		assertEquals(original.getRole(), parsed.getRole());
		assertEquals(original.getHoTen(), parsed.getHoTen());
	}
}
