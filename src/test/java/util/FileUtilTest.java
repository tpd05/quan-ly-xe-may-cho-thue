package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link FileUtil}.
 *
 * Dùng {@code @TempDir} để JUnit tự tạo thư mục tạm riêng cho mỗi test,
 * tránh đọc/ghi vào file dữ liệu thật của ứng dụng (data/*.csv) và tự
 * động dọn dẹp sau khi test xong.
 */
class FileUtilTest {

	@TempDir
	Path tempDir;

	@Test
	@DisplayName("exists() trả về false khi file chưa được tạo")
	void exists_false_khi_chua_tao() {
		String filePath = tempDir.resolve("chua_ton_tai.csv").toString();

		assertFalse(FileUtil.exists(filePath));
	}

	@Test
	@DisplayName("createIfNotExists() tạo file mới nếu chưa tồn tại")
	void tao_file_moi() {
		String filePath = tempDir.resolve("moi.csv").toString();

		FileUtil.createIfNotExists(filePath);

		assertTrue(FileUtil.exists(filePath));
	}

	@Test
	@DisplayName("createIfNotExists() không ghi đè file đã có nội dung")
	void khong_ghi_de_file_da_co() {
		String filePath = tempDir.resolve("da_co.csv").toString();
		FileUtil.createIfNotExists(filePath);
		FileUtil.writeAllLines(filePath, List.of("header", "data1"));

		// Gọi lại createIfNotExists trên file đã tồn tại
		FileUtil.createIfNotExists(filePath);

		assertEquals(List.of("header", "data1"), FileUtil.readAllLines(filePath));
	}

	@Test
	@DisplayName("writeAllLines() rồi readAllLines() trả về đúng nội dung đã ghi")
	void ghi_va_doc_dung_noi_dung() {
		String filePath = tempDir.resolve("test.csv").toString();
		List<String> lines = List.of("id,name", "1,Honda", "2,Yamaha");

		FileUtil.writeAllLines(filePath, lines);
		List<String> result = FileUtil.readAllLines(filePath);

		assertEquals(lines, result);
	}

	@Test
	@DisplayName("writeAllLines() ghi đè toàn bộ nội dung cũ")
	void ghi_de_toan_bo_noi_dung_cu() {
		String filePath = tempDir.resolve("ghi_de.csv").toString();

		FileUtil.writeAllLines(filePath, List.of("dong_cu_1", "dong_cu_2"));
		FileUtil.writeAllLines(filePath, List.of("dong_moi"));

		assertEquals(List.of("dong_moi"), FileUtil.readAllLines(filePath));
	}

	@Test
	@DisplayName("appendLine() thêm dòng vào cuối file, giữ nguyên dòng cũ")
	void them_dong_vao_cuoi_file() {
		String filePath = tempDir.resolve("append.csv").toString();
		FileUtil.writeAllLines(filePath, List.of("header"));

		FileUtil.appendLine(filePath, "dong_1");
		FileUtil.appendLine(filePath, "dong_2");

		assertEquals(List.of("header", "dong_1", "dong_2"), FileUtil.readAllLines(filePath));
	}

	@Test
	@DisplayName("writeAllLines() tự tạo thư mục cha nếu chưa tồn tại")
	void tu_tao_thu_muc_cha() {
		String filePath = tempDir.resolve("con/chau/data.csv").toString();

		FileUtil.writeAllLines(filePath, List.of("noi_dung"));

		assertTrue(FileUtil.exists(filePath));
	}
}
