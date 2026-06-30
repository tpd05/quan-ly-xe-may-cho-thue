package dao;

import java.util.List;

public interface DAO<T> {

	boolean insert(T t);

	boolean update(T t);

	boolean delete(int id);

	T findById(int id);

	List<T> findAll();
}