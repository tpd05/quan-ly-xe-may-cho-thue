package util;
import java.util.List;


public class IdGenerator {
	
	private IdGenerator() {

	}
	
	//Id tự động tăng
	public static int nextID(String filePath) {
		List<String> lines = FileUtil.readAllLines(filePath);

		if (lines.size() <= 1) {
			return 1;
		}

		int maxId = 0;
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
