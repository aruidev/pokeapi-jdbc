package model.dao;

public interface DAO<T,k> {
    void insertTable(T t);
    void readTable(k id);
    void updateTable(T t);
    void deleteTable(k id);
    void readAll();
}
