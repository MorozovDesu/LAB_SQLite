package MD.model;

public class Testee {
    private int id;
    private String nameTestee;
    private String nameTest;
    private String resultTest;

    public Testee(int id, String nameTest, String nameTestee, String resultTest) {
        this.id = id;
        this.setResultTest(resultTest);
        this.setNameTest(nameTest);
        this.setNameTestee(nameTestee);
    }

    public Testee(String nameTest, String nameTestee, String group_id) {
        this.setNameTest(nameTest);
        this.setNameTestee(nameTestee);
        this.setResultTest(group_id);
    }

    @Override
    public String toString() {
        return getId()+ ": "+ getNameTest() + ' ' + getNameTestee() + ' ' + " : " + getResultTest();
    }
    public int getId() {
        return this.id;
    }

    public String getNameTestee() {
        return nameTestee;
    }

    public void setNameTestee(String nameTestee) {
        this.nameTestee = nameTestee;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public String getResultTest() {
        return resultTest;
    }

    public void setResultTest(String resultTest) {
        this.resultTest = resultTest;
    }
}
