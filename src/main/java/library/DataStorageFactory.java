package library;

public class DataStorageFactory {
    private static DataStorage dataStorage;

    public static DataStorage getInstance() {
        if (dataStorage == null) {
            dataStorage = new MySQLDataStorage();
            dataStorage.initializeData();
        }
        return dataStorage;
    }
}
