package orm;


import java.util.List;

class cacheMetaDate<T> {

    public List<ItemField> listColumn = null;
    public ItemField keyColumn = null;
    String tableName = null;
    String where = null;
    private int isIAction = 0;
    private Class result = null;


    public cacheMetaDate(Class<T> aClass) {
        SetClass(aClass);
    }

    public boolean isIAction() {
        return isIAction == 1;
    }

    private void SetClass(Class tClass) {
        if (result == null) {
            result = tClass;
        }
        if (tableName == null) {
            tableName = AnotationOrm.getTableName(tClass);
        }
        if (where == null) {
            where = AnotationOrm.getWhere(tClass);
        }
        if (keyColumn == null) {
            keyColumn = AnotationOrm.getKeyName(tClass);
        }
        if (listColumn == null) {
            listColumn = AnotationOrm.getListColumn(tClass);
        }
        if (isIAction == 0) {
            isIAction = 2;
            for (Class aClass : tClass.getInterfaces()) {
                if (aClass == IActionOrm.class) {
                    isIAction = 1;
                }
            }
        }
    }


    public String[] getStringSelect() {
        String[] list = new String[listColumn.size() + 1];
        for (int i = 0; i < listColumn.size(); i++) {
            list[i] = listColumn.get(i).columnName;
        }
        if (keyColumn != null && keyColumn.columnName != null)
            list[listColumn.size()] = keyColumn.columnName;
        return list;
    }


}
