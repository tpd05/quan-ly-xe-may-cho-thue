package dao;

public interface Mapper<T> {

	T parse(String line); // chuyển 1 dòng của file dữ liệu thành đối tượng Java

	String format(T object); // chuyển đối tượng Java thành 1 dòng của file dữ liệu

}
