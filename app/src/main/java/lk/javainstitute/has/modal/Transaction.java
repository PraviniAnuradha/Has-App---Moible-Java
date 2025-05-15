package lk.javainstitute.has.modal;

public class Transaction {
    private int id;
    private String from;
    private String to;
    private double amount;
    private String dateTime;
    private String des;
    private String bank;

    public Transaction() {
    }

    public Transaction(int id, String from, String to, double amount, String dateTime, String des, String bank) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.dateTime = dateTime;
        this.des = des;
        this.bank = bank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
