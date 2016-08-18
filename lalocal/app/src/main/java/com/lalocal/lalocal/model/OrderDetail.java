package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/28.
 * 部分数据严重不足 注意更新数据类型
 */
public class OrderDetail implements Parcelable {


    /**
     * id : 1008
     * orderNumb : 1471490247742
     * fee : 874.0
     * originFee : 874.0
     * name : 私人订制之旅：含晚餐与演出的吉隆坡（Kuala Lumpur）文化之夜
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/2016050611121915155076137897
     * orderDate : 2016-08-18
     * createdTime : 2016-08-18 11:17:27
     * appraiseTime : null
     * payTime : null
     * payType : null
     * status : 0
     * couponValue : null
     * productionId : 3058
     * peopleItemList : [{"contactInfoList":[{"itemList":[{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"HHH","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"GGGG","type":5,"nessary":true}]},{"itemList":[{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"ZHANG","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"SAN","type":5,"nessary":true},{"description":"","code":"-1","name":"isLeader","value":"1","type":8,"nessary":true},{"description":"","code":"2","name":"email","value":"yyy@qq.com","type":0,"nessary":true},{"description":"","code":"12","name":"phone","value":"+86-1875888597","type":3,"nessary":true}]}],"name":"成人","amount":2,"id":17242}]
     * orderPayList : [{"name":"成人","unit":437,"amount":2}]
     * produItemList : [{"id":11672,"name":"私人订制之旅：含晚餐与演出的吉隆坡（Kuala Lumpur）文化之夜","category":"套餐种类","type":5,"description":"","needLeader":true,"contactLimit":1,"maxBookNum":9,"contactItems":[{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"","type":5,"nessary":true}],"produItemAdditionVOs":[],"langService":{}}]
     * creditStatus : null
     * billUrl : null
     * userName : GEGE GQ
     * email : 134@24.com
     * phone : +86-122555
     * pickUpCode :
     * pickUpPoint : Hgg
     * orderQuestions : []
     * refundLog : null
     * languageName :
     * remark : Hhhh
     * supplierId : null
     */

    private int id;
    private String orderNumb;
    private double fee;
    private double originFee;
    private String name;
    private String photo;
    private String orderDate;
    private String createdTime;
    private String appraiseTime;
    private String payTime;
    private String payType;
    private int status;
    private double couponValue;
    private int productionId;
    private Object creditStatus;
    private Object billUrl;
    private String userName;
    private String email;
    private String phone;
    private String pickUpCode;
    private String pickUpPoint;
    private Object refundLog;
    private String languageName;
    private String remark;
    private Object supplierId;
    /**
     * contactInfoList : [{"itemList":[{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"HHH","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"GGGG","type":5,"nessary":true}]},{"itemList":[{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"ZHANG","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"SAN","type":5,"nessary":true},{"description":"","code":"-1","name":"isLeader","value":"1","type":8,"nessary":true},{"description":"","code":"2","name":"email","value":"yyy@qq.com","type":0,"nessary":true},{"description":"","code":"12","name":"phone","value":"+86-1875888597","type":3,"nessary":true}]}]
     * name : 成人
     * amount : 2
     * id : 17242
     */

    private List<PeopleItemListBean> peopleItemList;
    /**
     * name : 成人
     * unit : 437.0
     * amount : 2
     */

    private List<OrderPayListBean> orderPayList;
    /**
     * id : 11672
     * name : 私人订制之旅：含晚餐与演出的吉隆坡（Kuala Lumpur）文化之夜
     * category : 套餐种类
     * type : 5
     * description :
     * needLeader : true
     * contactLimit : 1
     * maxBookNum : 9
     * contactItems : [{"description":"如张三填写为'zhang'","code":"0","name":"姓(拼音)","value":"","type":9,"nessary":true},{"description":"如张三填写为'san'","code":"1","name":"名(拼音)","value":"","type":5,"nessary":true}]
     * produItemAdditionVOs : []
     * langService : {}
     */

    private List<ProduItemListBean> produItemList;
    private List<?> orderQuestions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumb() {
        return orderNumb;
    }

    public void setOrderNumb(String orderNumb) {
        this.orderNumb = orderNumb;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getOriginFee() {
        return originFee;
    }

    public void setOriginFee(double originFee) {
        this.originFee = originFee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(double couponValue) {
        this.couponValue = couponValue;
    }

    public int getProductionId() {
        return productionId;
    }

    public void setProductionId(int productionId) {
        this.productionId = productionId;
    }

    public Object getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(Object creditStatus) {
        this.creditStatus = creditStatus;
    }

    public Object getBillUrl() {
        return billUrl;
    }

    public void setBillUrl(Object billUrl) {
        this.billUrl = billUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPickUpCode() {
        return pickUpCode;
    }

    public void setPickUpCode(String pickUpCode) {
        this.pickUpCode = pickUpCode;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public Object getRefundLog() {
        return refundLog;
    }

    public void setRefundLog(Object refundLog) {
        this.refundLog = refundLog;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Object supplierId) {
        this.supplierId = supplierId;
    }

    public List<PeopleItemListBean> getPeopleItemList() {
        return peopleItemList;
    }

    public void setPeopleItemList(List<PeopleItemListBean> peopleItemList) {
        this.peopleItemList = peopleItemList;
    }

    public List<OrderPayListBean> getOrderPayList() {
        return orderPayList;
    }

    public void setOrderPayList(List<OrderPayListBean> orderPayList) {
        this.orderPayList = orderPayList;
    }

    public List<ProduItemListBean> getProduItemList() {
        return produItemList;
    }

    public void setProduItemList(List<ProduItemListBean> produItemList) {
        this.produItemList = produItemList;
    }

    public List<?> getOrderQuestions() {
        return orderQuestions;
    }

    public void setOrderQuestions(List<?> orderQuestions) {
        this.orderQuestions = orderQuestions;
    }

    public static class PeopleItemListBean {
        private String name;
        private int amount;
        private int id;
        private List<ContactInfoListBean> contactInfoList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ContactInfoListBean> getContactInfoList() {
            return contactInfoList;
        }

        public void setContactInfoList(List<ContactInfoListBean> contactInfoList) {
            this.contactInfoList = contactInfoList;
        }

        public static class ContactInfoListBean implements Parcelable {
            /**
             * description : 如张三填写为'zhang'
             * code : 0
             * name : 姓(拼音)
             * value : HHH
             * type : 9
             * nessary : true
             */

            private List<ItemListBean> itemList;

            public List<ItemListBean> getItemList() {
                return itemList;
            }

            public void setItemList(List<ItemListBean> itemList) {
                this.itemList = itemList;
            }

            public static class ItemListBean implements Parcelable {
                private String description;
                private String code;
                private String name;
                private String value;
                private int type;
                private boolean nessary;

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public boolean isNessary() {
                    return nessary;
                }

                public void setNessary(boolean nessary) {
                    this.nessary = nessary;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.description);
                    dest.writeString(this.code);
                    dest.writeString(this.name);
                    dest.writeString(this.value);
                    dest.writeInt(this.type);
                    dest.writeByte(this.nessary ? (byte) 1 : (byte) 0);
                }

                public ItemListBean() {
                }

                protected ItemListBean(Parcel in) {
                    this.description = in.readString();
                    this.code = in.readString();
                    this.name = in.readString();
                    this.value = in.readString();
                    this.type = in.readInt();
                    this.nessary = in.readByte() != 0;
                }

                public static final Creator<ItemListBean> CREATOR = new Creator<ItemListBean>() {
                    @Override
                    public ItemListBean createFromParcel(Parcel source) {
                        return new ItemListBean(source);
                    }

                    @Override
                    public ItemListBean[] newArray(int size) {
                        return new ItemListBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeList(this.itemList);
            }

            public ContactInfoListBean() {
            }

            protected ContactInfoListBean(Parcel in) {
                this.itemList = new ArrayList<ItemListBean>();
                in.readList(this.itemList, ItemListBean.class.getClassLoader());
            }

            public static final Creator<ContactInfoListBean> CREATOR = new Creator<ContactInfoListBean>() {
                @Override
                public ContactInfoListBean createFromParcel(Parcel source) {
                    return new ContactInfoListBean(source);
                }

                @Override
                public ContactInfoListBean[] newArray(int size) {
                    return new ContactInfoListBean[size];
                }
            };
        }

    }

    public static class OrderPayListBean {
        private String name;
        private double unit;
        private int amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getUnit() {
            return unit;
        }

        public void setUnit(double unit) {
            this.unit = unit;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }


    }

    public static class ProduItemListBean {
        private int id;
        private String name;
        private String category;
        private int type;
        private String description;
        private boolean needLeader;
        private int contactLimit;
        private int maxBookNum;
        private LangServiceBean langService;
        /**
         * description : 如张三填写为'zhang'
         * code : 0
         * name : 姓(拼音)
         * value :
         * type : 9
         * nessary : true
         */

        private List<ContactItemsBean> contactItems;
        private List<?> produItemAdditionVOs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isNeedLeader() {
            return needLeader;
        }

        public void setNeedLeader(boolean needLeader) {
            this.needLeader = needLeader;
        }

        public int getContactLimit() {
            return contactLimit;
        }

        public void setContactLimit(int contactLimit) {
            this.contactLimit = contactLimit;
        }

        public int getMaxBookNum() {
            return maxBookNum;
        }

        public void setMaxBookNum(int maxBookNum) {
            this.maxBookNum = maxBookNum;
        }

        public LangServiceBean getLangService() {
            return langService;
        }

        public void setLangService(LangServiceBean langService) {
            this.langService = langService;
        }

        public List<ContactItemsBean> getContactItems() {
            return contactItems;
        }

        public void setContactItems(List<ContactItemsBean> contactItems) {
            this.contactItems = contactItems;
        }

        public List<?> getProduItemAdditionVOs() {
            return produItemAdditionVOs;
        }

        public void setProduItemAdditionVOs(List<?> produItemAdditionVOs) {
            this.produItemAdditionVOs = produItemAdditionVOs;
        }

        public static class LangServiceBean {
        }

        public static class ContactItemsBean {
            private String description;
            private String code;
            private String name;
            private String value;
            private int type;
            private boolean nessary;

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public boolean isNessary() {
                return nessary;
            }

            public void setNessary(boolean nessary) {
                this.nessary = nessary;
            }


        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.userName);
        dest.writeString(this.email);
        dest.writeString(this.phone);
    }

    public OrderDetail() {
    }

    protected OrderDetail(Parcel in) {
        this.id = in.readInt();
        this.name=in.readString();
        this.photo=in.readString();
        this.userName = in.readString();
        this.email = in.readString();
        this.phone = in.readString();

    }

    public static final Parcelable.Creator<OrderDetail> CREATOR = new Parcelable.Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}

