package MD.model;

public class Student {
    private int id;
    private String name;
    private String lastname;
    private String group;

    public Student(int id, String lastname, String name, String group) {
        this.id = id;
        this.setGroup(group);
        this.setLastname(lastname);
        this.setName(name);
    }

    public Student(String lastname, String name, String group_id) {
        this.setLastname(lastname);
        this.setName(name);
        this.setGroup(group_id);
    }

    @Override
    public String toString() {
        return getId()+ ": "+ getLastname() + ' ' + getName() + ' ' + " : " + getGroup();
    }
    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
