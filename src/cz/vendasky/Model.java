package cz.vendasky;

public class Model {

    private String selectedDatabase = "";
    private String selectedTable = "";

    static Model instace;

    private Model() {

    }

    public void setSelectedDatabase(String selectedDatabase) {
        this.selectedDatabase = selectedDatabase;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public String getSelectedDatabase() {
        return this.selectedDatabase;
    }

    public String getSelectedTable() {
        return this.selectedTable;
    }

    public static Model getInstace() {
        if (instace == null) {
            instace = new Model();
        }
        return instace;
    }

}
