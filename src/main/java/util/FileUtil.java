package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class FileUtil {
	private FileUtil() {

	}

	// Kiểm tra file có tồn tại hay không
	public static boolean exists(String filePath) {
		Path path = Paths.get(filePath);
		return Files.exists(path);
	}

	// Tạo file nếu chưa tồn tại
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

	// Đọc all file vào bộ nhớ và trả về danh sách các dòng
	public static List<String> readAllLines(String filePath) {
		Path path = Paths.get(filePath);

		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException("Không thể đọc file" + filePath, e);
		}
	}

	// Ghi đè toàn bộ file
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

	// Ghi thêm 1 dòng cuối file
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
