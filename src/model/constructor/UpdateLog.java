package model.constructor;

public class UpdateLog {
    private int id;
    private String tableName;
    private String action;
    private int rowId;
    private String changedAt;

    // Constructor
    public UpdateLog(int id, String tableName, String action, int rowId, String changedAt) {
        this.id = id;
        this.tableName = tableName;
        this.action = action;
        this.rowId = rowId;
        this.changedAt = changedAt;
    }
    // Getters (y setters si los necesitas)
    public int getId()              { return id; }
    public String getTableName()    { return tableName; }
    public String getAction()       { return action; }
    public int getRowId()           { return rowId; }
    public String getChangedAt()    { return changedAt; }
}
