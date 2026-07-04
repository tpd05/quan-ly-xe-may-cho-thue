package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Lớp tiện ích xử lý đọc/ghi file, dùng làm nền tảng lưu trữ dữ liệu
 * dạng CSV cho toàn bộ DAO trong hệ thống.
 */
public final class FileUtil {

	private FileUtil() {
	}

	/**
	 * Kiểm tra file có tồn tại hay không.
	 *
	 * @param filePath Đường dẫn file.
	 * @return true nếu file tồn tại.
	 */
	public static boolean exists(String filePath) {
		Path path = Paths.get(filePath);
		return Files.exists(path);
	}

	/**
	 * Tạo file nếu chưa tồn tại. Tự động tạo cả thư mục cha nếu cần.
	 *
	 * @param filePath Đường dẫn file cần tạo.
	 * @throws RuntimeException nếu không thể tạo file.
	 */
	public static void createIfNotExists(String filePath) {
		Path path = Paths.get(filePath);

		if (Files.notExists(path)) {
			try {
				Path parent = path.getParent();

				if (Files.notExists(parent)) {
					Files.createDirectories(parent);
				}

				Files.createFile(path);

			} catch (IOException e) {
				throw new RuntimeException("Không thể tạo file: " + filePath, e);
			}
		}
	}

	/**
	 * Đọc toàn bộ file vào bộ nhớ và trả về danh sách các dòng.
	 *
	 * @param filePath Đường dẫn file cần đọc.
	 * @return Danh sách các dòng trong file.
	 * @throws RuntimeException nếu không thể đọc file.
	 */
	public static List<String> readAllLines(String filePath) {
		Path path = Paths.get(filePath);

		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException("Không thể đọc file" + filePath, e);
		}
	}

	/**
	 * Ghi đè toàn bộ nội dung file bằng danh sách dòng mới.
	 *
	 * @param filePath Đường dẫn file cần ghi.
	 * @param lines    Danh sách các dòng cần ghi.
	 * @throws RuntimeException nếu không thể ghi file.
	 */
	public static void writeAllLines(String filePath, List<String> lines) {
		Path path = Paths.get(filePath);

		try {
			Path parent = path.getParent();

			if (parent != null && Files.notExists(parent)) {
				Files.createDirectories(parent);
			}

			Files.write(path, lines);

		} catch (IOException e) {
			throw new RuntimeException("Không thể ghi file: " + filePath, e);
		}
	}

	/**
	 * Ghi thêm 1 dòng vào cuối file (không ghi đè nội dung cũ).
	 *
	 * @param filePath Đường dẫn file cần ghi thêm.
	 * @param line     Dòng dữ liệu cần thêm.
	 * @throws RuntimeException nếu không thể ghi file.
	 */
	public static void appendLine(String filePath, String line) {
		Path path = Paths.get(filePath);

		try {
			Path parent = path.getParent();

			if (parent != null && Files.notExists(parent)) {
				Files.createDirectories(parent);
			}

			Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException("Không thể ghi thêm dữ liệu vào file: " + filePath, e);
		}
	}
}