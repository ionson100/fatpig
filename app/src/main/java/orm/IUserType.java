package orm;

public interface IUserType {
    Object getObject(String str);

    String getString(Object ojb);
}
