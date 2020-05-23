package Model;

import java.util.Date;

public class calendar {
    private int idCalendar; //идентификатор записи
    private Date dateOfRecording; //дата записи
    private String fio; //фио сотрудника
    private String nameMark; //наименование отметки

    public calendar() {
    }

    public calendar(int idCalendar, Date dateOfRecording, String fio, String nameMark) {
        this.idCalendar = idCalendar;
        this.dateOfRecording = dateOfRecording;
        this.fio = fio;
        this.nameMark = nameMark;
    }

    public int getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(int idCalendar) {
        this.idCalendar = idCalendar;
    }

    public Date getDateOfRecording() {
        return dateOfRecording;
    }

    public void setDateOfRecording(Date dateOfRecording) {
        this.dateOfRecording = dateOfRecording;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getNameMark() {
        return nameMark;
    }

    public void setNameMark(String nameMark) {
        this.nameMark = nameMark;
    }

}
