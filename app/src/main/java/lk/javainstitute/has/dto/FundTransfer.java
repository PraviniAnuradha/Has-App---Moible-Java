package lk.javainstitute.has.dto;

public class FundTransfer {
    private double amount;
    private String name;
    private String bank;
    private String accNo;
    private String des;

    public FundTransfer() {
    }

    public FundTransfer(double amount, String name, String bank, String accNo, String des) {
        this.amount = amount;
        this.name = name;
        this.bank = bank;
        this.accNo = accNo;
        this.des = des;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
