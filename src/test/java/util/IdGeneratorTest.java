package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit test cho {@link IdGenerator}.
 */
class IdGeneratorTest {

	@TempDir
	Path tempDir;

	@Test
	@DisplayName("File chỉ có header (chưa có dữ liệu) -> ID đầu tiên là 1")
	void file_chi_co_header() {
		String filePath = tempDir.resolve("empty.csv").toString();
		FileUtil.writeAllLines(filePath, List.of("id,name"));

		assertEquals(1, IdGenerator.nextID(filePath));
	}

	@Test
	@DisplayName("ID mới = ID lớn nhất hiện có + 1")
	void id_moi_bang_id_lon_nhat_cong_1() {
		String filePath = tempDir.resolve("data.csv").toString();
		FileUtil.writeAllLines(filePath, List.of(
				"id,name",
				"1,Honda",
				"3,Yamaha",
				"2,Suzuki"));

		// ID lớn nhất hiện có là 3 -> ID mới phải là 4
		assertEquals(4, IdGenerator.nextID(filePath));
	}

	@Test
	@DisplayName("Bỏ qua dòng trống khi tính ID lớn nhất")
	void bo_qua_dong_trong() {
		String filePath = tempDir.resolve("data_with_blank.csv").toString();
		FileUtil.writeAllLines(filePath, List.of(
				"id,name",
				"1,Honda",
				"",
				"5,Yamaha"));

		assertEquals(6, IdGenerator.nextID(filePath));
	}

	@Test
	@DisplayName("Dữ liệu chỉ có 1 dòng (đúng header, không có data) -> trả về 1")
	void chi_co_1_dong_header() {
		String filePath = tempDir.resolve("only_header.csv").toString();
		FileUtil.writeAllLines(filePath, List.of("id,name"));

		assertEquals(1, IdGenerator.nextID(filePath));
	}
}
