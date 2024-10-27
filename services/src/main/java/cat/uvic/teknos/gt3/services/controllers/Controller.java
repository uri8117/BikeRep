package cat.uvic.teknos.gt3.services.controllers;

public interface Controller<K, V> {

    String get(int id);
    String get();
    void post(String json);
    void put(int id, String json);
    void delete(int id);
}
