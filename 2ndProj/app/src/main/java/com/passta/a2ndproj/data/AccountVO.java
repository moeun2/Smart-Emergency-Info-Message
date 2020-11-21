package com.passta.a2ndproj.data;

public class AccountVO {


    private String socket_id;
    private String name;
    private String bank;
    private int accountNum;
    private int img;
    private int pay;

    public AccountVO()
    {

    }
    public AccountVO(String socket_id, String name, String bank, int accountNum, int img)
    {
        this.socket_id = socket_id;
        this.name = name;
        this.bank = bank;
        this.accountNum = accountNum;
        this.img = img;
    }


    public void setName(String name)
    {
        this.name = name;
    }
    public String getName(){ return name;}

    public void setBank(String bank)
    {
        this.bank = bank;
    }
    public String getBank(){ return bank;}

    public void setAccountNum(int accountNum)
    {
        this.accountNum = accountNum;
    }
    public int getAccountNum(){ return accountNum;}

    public void setImg(int img)
    {
        this.img = img;
    }
    public int getImg(){ return img;}

    public void setPay(int pay)
    {
        this.pay = pay;
    }
    public int getPay(){ return pay;}

    public void setSocket_id(String socket_id)
    {
        this.socket_id = socket_id;
    }
    public String getSocket_id(){ return socket_id;}
}
