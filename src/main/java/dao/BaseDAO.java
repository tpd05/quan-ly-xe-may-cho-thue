package dao;

import java.util.ArrayList;
import java.util.List;

import util.FileUtil;

/**
 * Lớp cha trừu tượng triển khai các thao tác CRUD dùng chung cho mọi DAO
 * lưu trữ dữ liệu dạng file CSV.
 *
 * @param <T> kiểu đối tượng được quản lý.
 */
public abstract class BaseDAO<T> implements DAO<T>, Mapper<T> {

	protected final String filePath;
	protected final String header;

	/**
	 * Khởi tạo DAO.
	 *
	 * Nếu file dữ liệu chưa tồn tại sẽ tự động tạo mới. Nếu file rỗng sẽ ghi dòng
	 * tiêu đề (header).
	 *
	 * @param filePath Đường dẫn file CSV.
	 * @param header   Dòng tiêu đề của file CSV.
	 */
	protected BaseDAO(String filePath, String header) {
		this.filePath = filePath;
		this.header = header;

		FileUtil.createIfNotExists(filePath);

		if (FileUtil.readAllLines(filePath).isEmpty()) {
			FileUtil.writeAllLines(filePath, List.of(header));
		}
	}

	/**
	 * Đọc toàn bộ các dòng trong file.
	 *
	 * @return Danh sách các dòng trong file.
	 */
	protected List<String> readLines() {
		return FileUtil.readAllLines(filePath);
	}

	/**
	 * Ghi đè toàn bộ nội dung file.
	 *
	 * @param lines Danh sách các dòng cần ghi.
	 */
	protected void writeLines(List<String> lines) {
		FileUtil.writeAllLines(filePath, lines);
	}

	/**
	 * Đọc dữ liệu từ file và chuyển thành danh sách đối tượng. Bỏ qua dòng đầu
	 * tiên (header).
	 *
	 * @return Danh sách đối tượng.
	 */
	protected List<T> loadObjects() {
		List<String> lines = readLines();
		List<T> res = new ArrayList<>();

		// Bắt đầu từ index 1 để bỏ qua dòng header
		for (int i = 1; i < lines.size(); i++) {
			res.add(parse(lines.get(i)));
		}

		return res;
	}

	/**
	 * Lưu danh sách đối tượng xuống file. Toàn bộ nội dung file cũ sẽ bị ghi đè.
	 *
	 * @param objects Danh sách đối tượng cần lưu.
	 */
	protected void saveObjects(List<T> objects) {
		List<String> lines = new ArrayList<>();
		lines.add(header);

		for (T obj : objects) {
			lines.add(format(obj));
		}

		writeLines(lines);
	}

	/**
	 * Lấy khóa chính (ID) của đối tượng. Mỗi DAO con sẽ tự định nghĩa ID tương
	 * ứng.
	 *
	 * @param object Đối tượng cần lấy ID.
	 * @return Giá trị ID.
	 */
	protected abstract int getId(T object);

	/**
	 * Lấy toàn bộ dữ liệu.
	 *
	 * @return Danh sách tất cả đối tượng.
	 */
	@Override
	public List<T> findAll() {
		return loadObjects();
	}

	/**
	 * Tìm đối tượng theo ID.
	 *
	 * @param id ID cần tìm.
	 * @return Đối tượng nếu tồn tại, ngược lại trả về null.
	 */
	@Override
	public T findById(int id) {
		List<T> objects = loadObjects();

		for (T object : objects) {
			if (getId(object) == id) {
				return object;
			}
		}
		return null;
	}

	/**
	 * Thêm một đối tượng mới vào cuối file.
	 *
	 * @param object Đối tượng cần thêm.
	 * @return true nếu thêm thành công.
	 */
	@Override
	public boolean insert(T object) {
		FileUtil.appendLine(filePath, format(object));
		return true;
	}

	/**
	 * Cập nhật thông tin của một đối tượng theo ID.
	 *
	 * @param upObject Đối tượng chứa dữ liệu mới.
	 * @return true nếu cập nhật thành công, false nếu không tìm thấy ID.
	 */
	@Override
	public boolean update(T upObject) {
		List<T> objects = loadObjects();

		for (int i = 0; i < objects.size(); i++) {
			if (getId(objects.get(i)) == getId(upObject)) {
				objects.set(i, upObject);
				saveObjects(objects);
				return true;
			}
		}
		return false;
	}

	/**
	 * Xóa đối tượng theo ID.
	 *
	 * @param id ID cần xóa.
	 * @return true nếu xóa thành công, false nếu không tìm thấy ID.
	 */
	@Override
	public boolean delete(int id) {
		List<T> objects = loadObjects();

		for (int i = 0; i < objects.size(); i++) {
			if (getId(objects.get(i)) == id) {
				objects.remove(i);
				saveObjects(objects);
				return true;
			}
		}
		return false;
	}
}