package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/11/16.
 */

public class CmbPay {

    /**
     * branchId : 0571
     * coNo : 001238
     * billNo : 9281652705
     * amount : 3651.0
     * date : 20161116
     * expireTimeSpan : 30
     * merchantUrl : http://121.40.157.49/api/pay/cmb/result
     * merchantPara : userId=10078
     * merchantCode : |VULjTYWpNBY9rTo7j/22ay2nLcFqwMTTe*WIrvNIUyS/cQMXgFM5X3JHB3vbxJz46BlaOnIh*ALjjSZX64RsL5rv1SqcL4jbe26vem70Esqajz6K*HgCmnfB*OlJ09w/jIkki0CofOq6tb8IK9bo6lu8vvTKJ/P48/juwAxETuHCbPgzugCI9YUTosvQ36YNfaLBmM9FlGgJqTAuc8/2qtxwNpVpVuHSOR3jpI9bt2csZH7gSkjNnCxITA861Yo1arCcwlAwXGIk94Y7gAgT8u8OCb21jFBxzM99iarzlFj1I6z6U7QGMggjOxu5cw1gkxaIC*qW4DFGq0syBXvi0Hiyt8Um*1pOi63APQIw8uiwxKpwlW50Zg/VuIf7l7qmLy8uvEEhHDU9ndizH*MLqCLdbf65rLIjA/pGtTiFWymxmPLVFFTCz9gcPOiUxI1EVo4Er17DX/k6wGoCUFRLRQ==|adff445a1ef865cca60e9ef0afa62bb98905ac5d
     * merchantRetUrl : https://dev.lalocal.cn/wechat/cmbPaySuccess
     * merchantRetPara : null
     */


    private String branchId;
    private String coNo;
    private String billNo;
    private String amount;
    private String date;
    private int expireTimeSpan;
    private String merchantUrl;
    private String merchantPara;
    private String merchantCode;
    private String merchantRetUrl;
    private String merchantRetPara;

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCoNo() {
        return coNo;
    }

    public void setCoNo(String coNo) {
        this.coNo = coNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getExpireTimeSpan() {
        return expireTimeSpan;
    }

    public void setExpireTimeSpan(int expireTimeSpan) {
        this.expireTimeSpan = expireTimeSpan;
    }

    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }

    public String getMerchantPara() {
        return merchantPara;
    }

    public void setMerchantPara(String merchantPara) {
        this.merchantPara = merchantPara;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantRetUrl() {
        return merchantRetUrl;
    }

    public void setMerchantRetUrl(String merchantRetUrl) {
        this.merchantRetUrl = merchantRetUrl;
    }

    public String getMerchantRetPara() {
        if (merchantRetPara==null){
            merchantRetPara="";
        }
        return merchantRetPara;
    }

    public void setMerchantRetPara(String merchantRetPara) {
        this.merchantRetPara = merchantRetPara;
    }
}
