package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import model.ChiTietDonThue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link ChiTietDonThueDAO}.
 *
 * Dùng constructor phụ {@code ChiTietDonThueDAO(String filePath)} kèm
 * {@code @TempDir} để trỏ vào file CSV tạm, tránh đọc/ghi vào file dữ liệu thật.
 */
class ChiTietDonThueDAOTest {

	@TempDir
	Path tempDir;

	private ChiTietDonThueDAO dao;

	private static final LocalDateTime BAT_DAU = LocalDateTime.of(2026, 1, 1, 8, 0);
	private static final LocalDateTime KET_THUC = LocalDateTime.of(2026, 1, 3, 8, 0);

	@BeforeEach
	void setUp() {
		String filePath = tempDir.resolve("chitiet_test.csv").toString();
		dao = new ChiTietDonThueDAO(filePath);
	}

	/** Tạo chi tiết đơn thuê mẫu, xe CHƯA được trả (thoiGianTra = null). */
	private ChiTietDonThue taoChiTietChuaTra(int maChiTiet, int maDonThue, int maXe) {
		return new ChiTietDonThue(maChiTiet, maDonThue, maXe, BAT_DAU, KET_THUC, null, 100000);
	}

	/** Tạo chi tiết đơn thuê mẫu, xe ĐÃ được trả. */
	private ChiTietDonThue taoChiTietDaTra(int maChiTiet, int maDonThue, int maXe, LocalDateTime thoiGianTra) {
		return new ChiTietDonThue(maChiTiet, maDonThue, maXe, BAT_DAU, KET_THUC, thoiGianTra, 100000);
	}

	@Test
	@DisplayName("findAll() trả về danh sách rỗng khi file mới khởi tạo")
	void findAll_rong_khi_moi_khoi_tao() {
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("insert() với thoiGianTra = null (xe chưa trả) hoạt động đúng")
	void insert_khi_chua_tra_xe() {
		ChiTietDonThue ct = taoChiTietChuaTra(1, 100, 5);

		assertTrue(dao.insert(ct));

		ChiTietDonThue saved = dao.findById(1);
		assertNull(saved.getThoiGianTra());
		assertEquals(100, saved.getMaDonThue());
		assertEquals(5, saved.getMaXe());
		assertEquals(100000, saved.getDonGia());
	}

	@Test
	@DisplayName("insert() với thoiGianTra có giá trị (đã trả xe) hoạt động đúng")
	void insert_khi_da_tra_xe() {
		LocalDateTime thoiGianTra = LocalDateTime.of(2026, 1, 3, 9, 30);
		ChiTietDonThue ct = taoChiTietDaTra(1, 100, 5, thoiGianTra);

		dao.insert(ct);

		ChiTietDonThue saved = dao.findById(1);
		assertEquals(thoiGianTra, saved.getThoiGianTra());
	}

	@Test
	@DisplayName("format() trả về chuỗi rỗng ở cột thoiGianTra khi chưa trả xe")
	void format_thoiGianTra_rong_khi_chua_tra() {
		ChiTietDonThue ct = taoChiTietChuaTra(1, 100, 5);

		String line = dao.format(ct);

		assertEquals("1,100,5," + BAT_DAU + "," + KET_THUC + ",,100000", line);
	}

	@Test
	@DisplayName("Toàn vẹn dữ liệu qua chu kỳ format -> parse khi chưa trả xe")
	void round_trip_khi_chua_tra_xe() {
		ChiTietDonThue original = taoChiTietChuaTra(1, 100, 5);

		ChiTietDonThue parsed = dao.parse(dao.format(original));

		assertEquals(original.getMaChiTiet(), parsed.getMaChiTiet());
		assertEquals(original.getMaDonThue(), parsed.getMaDonThue());
		assertEquals(original.getMaXe(), parsed.getMaXe());
		assertEquals(original.getThoiGianBatDau(), parsed.getThoiGianBatDau());
		assertEquals(original.getThoiGianKetThuc(), parsed.getThoiGianKetThuc());
		assertNull(parsed.getThoiGianTra());
		assertEquals(original.getDonGia(), parsed.getDonGia());
	}

	@Test
	@DisplayName("Toàn vẹn dữ liệu qua chu kỳ format -> parse khi đã trả xe")
	void round_trip_khi_da_tra_xe() {
		LocalDateTime thoiGianTra = LocalDateTime.of(2026, 1, 3, 9, 30);
		ChiTietDonThue original = taoChiTietDaTra(1, 100, 5, thoiGianTra);

		ChiTietDonThue parsed = dao.parse(dao.format(original));

		assertEquals(thoiGianTra, parsed.getThoiGianTra());
	}

	@Test
	@DisplayName("parse() ném IllegalArgumentException khi thiếu cột dữ liệu")
	void parse_du_lieu_khong_hop_le() {
		assertThrows(IllegalArgumentException.class, () -> dao.parse("1,100,5,dữ liệu sai"));
	}

	@Test
	@DisplayName("delete() xóa đúng chi tiết theo mã chi tiết")
	void delete_thanh_cong() {
		dao.insert(taoChiTietChuaTra(1, 100, 5));
		dao.insert(taoChiTietChuaTra(2, 100, 6));

		assertTrue(dao.delete(1));

		List<ChiTietDonThue> conLai = dao.findAll();
		assertEquals(1, conLai.size());
		assertEquals(2, conLai.get(0).getMaChiTiet());
	}

	@Test
	@DisplayName("delete() trả về false khi mã chi tiết không tồn tại")
	void delete_khong_ton_tai() {
		dao.insert(taoChiTietChuaTra(1, 100, 5));

		assertFalse(dao.delete(999));
	}

	@Test
	@DisplayName("replaceAll() ghi đè toàn bộ nội dung file bằng danh sách mới")
	void replaceAll_ghi_de_toan_bo() {
		dao.insert(taoChiTietChuaTra(1, 100, 5));
		dao.insert(taoChiTietChuaTra(2, 100, 6));
		dao.insert(taoChiTietChuaTra(3, 200, 7));

		// Chỉ giữ lại chi tiết có mã 3
		List<ChiTietDonThue> danhSachMoi = List.of(taoChiTietChuaTra(3, 200, 7));
		dao.replaceAll(danhSachMoi);

		List<ChiTietDonThue> ketQua = dao.findAll();
		assertEquals(1, ketQua.size());
		assertEquals(3, ketQua.get(0).getMaChiTiet());
	}

	@Test
	@DisplayName("replaceAll() với danh sách rỗng -> file chỉ còn header")
	void replaceAll_voi_danh_sach_rong() {
		dao.insert(taoChiTietChuaTra(1, 100, 5));

		dao.replaceAll(List.of());

		assertTrue(dao.findAll().isEmpty());
	}
}
