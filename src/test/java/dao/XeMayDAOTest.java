package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import model.XeMay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link XeMayDAO}.
 *
 * Dùng constructor phụ {@code XeMayDAO(String filePath)} kèm {@code @TempDir}
 * để trỏ vào file CSV tạm, tránh đọc/ghi vào file dữ liệu thật của ứng dụng
 * (data/xemay.csv). Mỗi test được cấp 1 file riêng nên không ảnh hưởng nhau.
 */
class XeMayDAOTest {

	@TempDir
	Path tempDir;

	private XeMayDAO dao;

	@BeforeEach
	void setUp() {
		String filePath = tempDir.resolve("xemay_test.csv").toString();
		dao = new XeMayDAO(filePath);
	}

	/** Tạo 1 xe mẫu dùng chung cho các test. */
	private XeMay taoXeMau(int maXe, int userID, String bienSo) {
		return new XeMay(maXe, userID, "Honda", "Wave Alpha", 2022, 110f, bienSo,
				XeMay.TrangThai.SAN_SANG, BigDecimal.valueOf(100000), BigDecimal.valueOf(600000));
	}

	@Test
	@DisplayName("findAll() trả về danh sách rỗng khi file mới khởi tạo (chỉ có header)")
	void findAll_rong_khi_moi_khoi_tao() {
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("insert() thêm xe thành công, findAll() thấy đúng dữ liệu")
	void insert_va_findAll() {
		XeMay xe = taoXeMau(1, 10, "30A-12345");

		boolean result = dao.insert(xe);

		assertTrue(result);
		List<XeMay> danhSach = dao.findAll();
		assertEquals(1, danhSach.size());

		XeMay saved = danhSach.get(0);
		assertEquals(1, saved.getMaXe());
		assertEquals(10, saved.getUserID());
		assertEquals("Honda", saved.getHangXe());
		assertEquals("Wave Alpha", saved.getDongXe());
		assertEquals(2022, saved.getNamSanXuat());
		assertEquals(110f, saved.getDungTich());
		assertEquals("30A-12345", saved.getBienSo());
		assertEquals(XeMay.TrangThai.SAN_SANG, saved.getTrangThai());
		assertEquals(BigDecimal.valueOf(100000), saved.getGiaNgay());
		assertEquals(BigDecimal.valueOf(600000), saved.getGiaTuan());
	}

	@Test
	@DisplayName("findById() tìm thấy xe đã tồn tại")
	void findById_tim_thay() {
		dao.insert(taoXeMau(5, 10, "30A-11111"));

		XeMay found = dao.findById(5);

		assertEquals(5, found.getMaXe());
		assertEquals("30A-11111", found.getBienSo());
	}

	@Test
	@DisplayName("findById() trả về null khi không tồn tại")
	void findById_khong_tim_thay() {
		assertNull(dao.findById(999));
	}

	@Test
	@DisplayName("update() cập nhật thành công khi ID đã tồn tại")
	void update_thanh_cong() {
		dao.insert(taoXeMau(1, 10, "30A-12345"));

		XeMay xeMoi = taoXeMau(1, 10, "30A-99999");
		xeMoi.setHangXe("Yamaha");

		boolean result = dao.update(xeMoi);

		assertTrue(result);
		XeMay found = dao.findById(1);
		assertEquals("Yamaha", found.getHangXe());
		assertEquals("30A-99999", found.getBienSo());
	}

	@Test
	@DisplayName("update() trả về false khi ID không tồn tại, dữ liệu cũ không đổi")
	void update_khong_ton_tai() {
		dao.insert(taoXeMau(1, 10, "30A-12345"));

		XeMay xeKhongTonTai = taoXeMau(999, 10, "30A-00000");
		boolean result = dao.update(xeKhongTonTai);

		assertFalse(result);
		assertEquals(1, dao.findAll().size());
	}

	@Test
	@DisplayName("delete() xóa thành công khi ID đã tồn tại")
	void delete_thanh_cong() {
		dao.insert(taoXeMau(1, 10, "30A-12345"));

		boolean result = dao.delete(1);

		assertTrue(result);
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	@DisplayName("delete() trả về false khi ID không tồn tại")
	void delete_khong_ton_tai() {
		dao.insert(taoXeMau(1, 10, "30A-12345"));

		boolean result = dao.delete(999);

		assertFalse(result);
		assertEquals(1, dao.findAll().size());
	}

	@Test
	@DisplayName("parse() ném IllegalArgumentException khi dòng dữ liệu thiếu cột")
	void parse_du_lieu_khong_hop_le() {
		assertThrows(IllegalArgumentException.class, () -> dao.parse("1,10,Honda,Wave"));
	}

	@Test
	@DisplayName("format() trả về đúng chuỗi CSV theo đúng thứ tự cột")
	void format_dung_dinh_dang() {
		XeMay xe = taoXeMau(1, 10, "30A-12345");

		String line = dao.format(xe);

		assertEquals("1,10,Honda,Wave Alpha,2022,110.0,30A-12345,SAN_SANG,100000,600000", line);
	}

	@Test
	@DisplayName("insert() nhiều xe rồi findAll() trả về đúng số lượng và đúng thứ tự")
	void insert_nhieu_xe() {
		dao.insert(taoXeMau(1, 10, "30A-11111"));
		dao.insert(taoXeMau(2, 10, "30A-22222"));
		dao.insert(taoXeMau(3, 20, "30A-33333"));

		List<XeMay> danhSach = dao.findAll();

		assertEquals(3, danhSach.size());
		assertEquals(1, danhSach.get(0).getMaXe());
		assertEquals(2, danhSach.get(1).getMaXe());
		assertEquals(3, danhSach.get(2).getMaXe());
	}
}
