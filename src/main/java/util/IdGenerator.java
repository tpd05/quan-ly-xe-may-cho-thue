package util;

import java.util.List;

/**
 * Lớp tiện ích sinh ID tự động tăng (auto-increment) dựa trên dữ liệu
 * hiện có trong file CSV.
 */
public final class IdGenerator {

	private IdGenerator() {
	}

	/**
	 * Sinh ID mới cho file dữ liệu, bằng giá trị ID lớn nhất hiện có + 1.
	 * Nếu file chỉ có header (chưa có dữ liệu), trả về 1.
	 *
	 * @param filePath Đường dẫn file dữ liệu cần sinh ID.
	 * @return ID mới, chưa từng được sử dụng trong file.
	 */
	public static int nextID(String filePath) {
		List<String> lines = FileUtil.readAllLines(filePath);

		if (lines.size() <= 1) {
			return 1;
		}

		int maxId = 0;

		// Bắt đầu từ index 1 để bỏ qua dòng header
		for (int i = 1; i < lines.size(); i++) {
			String line = lines.get(i);

			if (line.isBlank()) {
				continue;
			}

			String[] data = line.split(",");
			maxId = Math.max(maxId, Integer.parseInt(data[0]));
		}

		return maxId + 1;
	}
}