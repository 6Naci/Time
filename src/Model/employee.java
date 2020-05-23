package Model;

import java.util.Date;

public class employee {
    private int id;
    private String fio;
    private String nameDep; //название департамента
    private String namePos; //название должности
    private Date dob; // дата рождения
    private String address;
    private boolean remotework = false;

    public employee() {
    }

    public employee(int id, String fio, Date dob, String nameDep, String namePos, String address, boolean remotework) {
        this.id = id;
        this.fio = fio;
        this.dob = dob;
        this.nameDep = nameDep;
        this.namePos = namePos;
        this.address = address;
        this.remotework = remotework;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isRemotework() {
        return remotework;
    }

    public void setRemotework(boolean remotework) {
        this.remotework = remotework;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamePos() {
        return namePos;
    }

    public void setNamePos(String namePos) {
        this.namePos = namePos;
    }

    public String getNameDep() {
        return nameDep;
    }

    public void setNameDep(String nameDep) {
        this.nameDep = nameDep;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
