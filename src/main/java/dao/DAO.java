package dao;

import java.util.List;

/**
 * Interface định nghĩa các thao tác CRUD cơ bản cho một entity.
 *
 * @param <T> kiểu đối tượng được quản lý.
 */
public interface DAO<T> {

	/** Thêm một đối tượng mới. */
	boolean insert(T t);

	/** Cập nhật đối tượng theo ID. */
	boolean update(T t);

	/** Xóa đối tượng theo ID. */
	boolean delete(int id);

	/** Tìm đối tượng theo ID. */
	T findById(int id);

	/** Lấy toàn bộ danh sách đối tượng. */
	List<T> findAll();
}