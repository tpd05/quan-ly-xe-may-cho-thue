package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import model.DonThue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link DonThueDAO}.
 *
 * Dùng constructor phụ {@code DonThueDAO(String filePath)} kèm {@code @TempDir}
 * để trỏ vào file CSV tạm, tránh đọc/ghi vào file dữ liệu thật.
 */
class DonThueDAOTest {

	@TempDir
	Path tempDir;

	private DonThueDAO dao;

	private static final LocalDateTime NGAY_DAT = LocalDateTime.of(2026, 1, 1, 8, 0);

	@BeforeEach
	void setUp() {
		String filePath = tempDir.resolve("donthue_test.csv").toString();
		dao = new DonThueDAO(filePath);
	}

	private DonThue taoDonMau(int maDonThue, int userID, DonThue.TrangThai trangThai) {
		return new DonThue(maDonThue, userID, "123 Đường ABC", trangThai, NGAY_DAT);
	}

	@Test
	@DisplayName("findAll() trả về danh sách rỗng khi file mới khởi tạo")
	void findAll_rong_khi_moi_khoi_tao() {
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("insert() thêm đơn thuê thành công, findAll() thấy đúng dữ liệu")
	void insert_va_findAll() {
		DonThue don = taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN);

		assertTrue(dao.insert(don));

		List<DonThue> danhSach = dao.findAll();
		assertEquals(1, danhSach.size());

		DonThue saved = danhSach.get(0);
		assertEquals(1, saved.getMaDonThue());
		assertEquals(10, saved.getUserID());
		assertEquals("123 Đường ABC", saved.getDiaChiNhanXe());
		assertEquals(DonThue.TrangThai.CHO_XAC_NHAN, saved.getTrangThai());
		assertEquals(NGAY_DAT, saved.getNgayDat());
	}

	@Test
	@DisplayName("findById() tìm thấy đơn thuê đã tồn tại")
	void findById_tim_thay() {
		dao.insert(taoDonMau(5, 10, DonThue.TrangThai.DANG_THUE));

		DonThue found = dao.findById(5);

		assertEquals(5, found.getMaDonThue());
		assertEquals(DonThue.TrangThai.DANG_THUE, found.getTrangThai());
	}

	@Test
	@DisplayName("findById() trả về null khi không tồn tại")
	void findById_khong_tim_thay() {
		assertNull(dao.findById(999));
	}

	@Test
	@DisplayName("update() cập nhật trạng thái đơn thuê thành công")
	void update_thanh_cong() {
		dao.insert(taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN));

		DonThue donMoi = taoDonMau(1, 10, DonThue.TrangThai.HOAN_THANH);
		boolean result = dao.update(donMoi);

		assertTrue(result);
		assertEquals(DonThue.TrangThai.HOAN_THANH, dao.findById(1).getTrangThai());
	}

	@Test
	@DisplayName("update() trả về false khi ID không tồn tại")
	void update_khong_ton_tai() {
		dao.insert(taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN));

		boolean result = dao.update(taoDonMau(999, 10, DonThue.TrangThai.DA_HUY));

		assertFalse(result);
	}

	@Test
	@DisplayName("delete() xóa thành công khi ID đã tồn tại")
	void delete_thanh_cong() {
		dao.insert(taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN));

		assertTrue(dao.delete(1));
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("delete() trả về false khi ID không tồn tại")
	void delete_khong_ton_tai() {
		dao.insert(taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN));

		assertFalse(dao.delete(999));
	}

	@Test
	@DisplayName("parse() ném IllegalArgumentException khi thiếu cột dữ liệu")
	void parse_du_lieu_khong_hop_le() {
		assertThrows(IllegalArgumentException.class, () -> dao.parse("1,10,Địa chỉ"));
	}

	@Test
	@DisplayName("parse() ném exception khi trạng thái không khớp enum")
	void parse_trang_thai_khong_hop_le() {
		String line = "1,10,Địa chỉ,KHONG_TON_TAI," + NGAY_DAT;
		assertThrows(IllegalArgumentException.class, () -> dao.parse(line));
	}

	@Test
	@DisplayName("format() trả về đúng chuỗi CSV theo đúng thứ tự cột")
	void format_dung_dinh_dang() {
		DonThue don = taoDonMau(1, 10, DonThue.TrangThai.CHO_XAC_NHAN);

		String line = dao.format(don);

		assertEquals("1,10,123 Đường ABC,CHO_XAC_NHAN," + NGAY_DAT, line);
	}

	@Test
	@DisplayName("Toàn vẹn dữ liệu qua chu kỳ format -> parse (round-trip)")
	void round_trip_format_parse() {
		DonThue original = taoDonMau(7, 20, DonThue.TrangThai.DANG_THUE);

		String line = dao.format(original);
		DonThue parsed = dao.parse(line);

		assertEquals(original.getMaDonThue(), parsed.getMaDonThue());
		assertEquals(original.getUserID(), parsed.getUserID());
		assertEquals(original.getDiaChiNhanXe(), parsed.getDiaChiNhanXe());
		assertEquals(original.getTrangThai(), parsed.getTrangThai());
		assertEquals(original.getNgayDat(), parsed.getNgayDat());
	}
}
