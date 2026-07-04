package dao;

/**
 * Interface chuyển đổi qua lại giữa 1 dòng dữ liệu (String) trong file
 * và 1 đối tượng Java tương ứng.
 *
 * @param <T> kiểu đối tượng cần chuyển đổi.
 */
public interface Mapper<T> {

	/** Chuyển 1 dòng của file dữ liệu thành đối tượng Java. */
	T parse(String line);

	/** Chuyển đối tượng Java thành 1 dòng của file dữ liệu. */
	String format(T object);
}