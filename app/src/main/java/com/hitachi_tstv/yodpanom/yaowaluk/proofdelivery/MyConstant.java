package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

/**
 * Created by musz on 10/11/2016.
 */

public class MyConstant {
    //Explicit
    private int iconAnInt = R.drawable.doremon48;// Image for icon 48x48
    private String titleHaveSpaceString = "มีช่องว่าง";
    private String messageHaveeSpaceString = "กรุณากรอกข้อมูลให้ครบทุกช่อง ค่ะ!";

    private  String urlUserString  = "http://service.eternity.co.th/TmsPXD/app/CenterService/getUser.php";


    public String getUrlUserString() {
        return urlUserString;
    }

    public int getIconAnInt() {
        return iconAnInt;
    }

    public String getTitleHaveSpaceString() {
        return titleHaveSpaceString;
    }

    public String getMessageHaveeSpaceString() {
        return messageHaveeSpaceString;
    }
}//Main Class
