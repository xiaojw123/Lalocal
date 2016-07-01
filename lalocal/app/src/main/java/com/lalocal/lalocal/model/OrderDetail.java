package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/28.
 */
public class OrderDetail {

    /**
     * id : 186
     * orderNumb : 1466586493457
     * fee : 0
     * originFee : 676
     * name : 威拉米特葡萄酒乡单车之旅（波特兰出发）
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/201605261253025729829081676
     * orderDate : 2016-06-30
     * createdTime : 2016-06-22 17:08:03
     * appraiseTime : null
     * payTime : 2016-06-22 17:08:03
     * payType : null
     * status : 2
     * couponValue : 676
     * productionId : 15605
     * peopleItemList : [{"contactInfoList":[{"itemList":[{"description":"","name":"姓名","value":"Hh","type":5,"nessary":true},{"description":"","name":"拼音","value":"Hhg","type":0,"nessary":true},{"description":"","name":"性别","value":"男","type":1,"nessary":true},{"description":"","name":"邮件","value":"Hh","type":0,"nessary":true},{"description":"","name":"电话","value":"+86 1111","type":3,"nessary":true}]}],"name":"成人1人票","amount":1,"id":42583}]
     * orderPayList : []
     * produItemList : [{"id":42582,"name":"标准行程","category":"套餐种类","type":5,"description":"Willamette wine bike tour. Hotel pickup and drop-off not included.","contactLimit":1,"contactItems":[{"description":"","name":"姓名","value":"","type":5,"nessary":true},{"description":"","name":"拼音","value":"","type":0,"nessary":true},{"description":"","name":"性别","value":"","type":1,"nessary":true},{"description":"","name":"邮件","value":"","type":0,"nessary":true},{"description":"","name":"电话","value":"","type":3,"nessary":true},{"description":"","name":"乘客身高：出于安全考虑，请填写所有乘客的个人身高，请用英寸或厘米表示(例如：5'2\", 158cm等等)","value":"","type":0,"nessary":true}],"produItemAdditionVOs":null}]
     */

    private int id;
    private String orderNumb;
    private int fee;
    private int originFee;
    private String name;
    private String photo;
    private String orderDate;
    private String createdTime;
    private String appraiseTime;
    private String payTime;
    private String payType;
    private int status;
    private int couponValue;
    private int productionId;
    /**
     * contactInfoList : [{"itemList":[{"description":"","name":"姓名","value":"Hh","type":5,"nessary":true},{"description":"","name":"拼音","value":"Hhg","type":0,"nessary":true},{"description":"","name":"性别","value":"男","type":1,"nessary":true},{"description":"","name":"邮件","value":"Hh","type":0,"nessary":true},{"description":"","name":"电话","value":"+86 1111","type":3,"nessary":true}]}]
     * name : 成人1人票
     * amount : 1
     * id : 42583
     */

    private List<PeopleItemListBean> peopleItemList;
    private List<OrderItem.OrderPay> orderPayList;
    /**
     * id : 42582
     * name : 标准行程
     * category : 套餐种类
     * type : 5
     * description : Willamette wine bike tour. Hotel pickup and drop-off not included.
     * contactLimit : 1
     * contactItems : [{"description":"","name":"姓名","value":"","type":5,"nessary":true},{"description":"","name":"拼音","value":"","type":0,"nessary":true},{"description":"","name":"性别","value":"","type":1,"nessary":true},{"description":"","name":"邮件","value":"","type":0,"nessary":true},{"description":"","name":"电话","value":"","type":3,"nessary":true},{"description":"","name":"乘客身高：出于安全考虑，请填写所有乘客的个人身高，请用英寸或厘米表示(例如：5'2\", 158cm等等)","value":"","type":0,"nessary":true}]
     * produItemAdditionVOs : null
     */

    private List<ProduItemListBean> produItemList;

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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getOriginFee() {
        return originFee;
    }

    public void setOriginFee(int originFee) {
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

    public int getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(int couponValue) {
        this.couponValue = couponValue;
    }

    public int getProductionId() {
        return productionId;
    }

    public void setProductionId(int productionId) {
        this.productionId = productionId;
    }

    public List<PeopleItemListBean> getPeopleItemList() {
        return peopleItemList;
    }

    public void setPeopleItemList(List<PeopleItemListBean> peopleItemList) {
        this.peopleItemList = peopleItemList;
    }

    public List<OrderItem.OrderPay> getOrderPayList() {
        return orderPayList;
    }

    public void setOrderPayList(List<OrderItem.OrderPay> orderPayList) {
        this.orderPayList = orderPayList;
    }

    public List<ProduItemListBean> getProduItemList() {
        return produItemList;
    }

    public void setProduItemList(List<ProduItemListBean> produItemList) {
        this.produItemList = produItemList;
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
             * description :
             * name : 姓名
             * value : Hh
             * type : 5
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
                    dest.writeString(this.name);
                    dest.writeString(this.value);
                    dest.writeInt(this.type);
                    dest.writeByte(this.nessary ? (byte) 1 : (byte) 0);
                }

                public ItemListBean() {
                }

                protected ItemListBean(Parcel in) {
                    this.description = in.readString();
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

            public static final Parcelable.Creator<ContactInfoListBean> CREATOR = new Parcelable.Creator<ContactInfoListBean>() {
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

    public static class ProduItemListBean {
        private int id;
        private String name;
        private String category;
        private int type;
        private String description;
        private int contactLimit;
        private Object produItemAdditionVOs;
        /**
         * description :
         * name : 姓名
         * value :
         * type : 5
         * nessary : true
         */

        private List<ContactItemsBean> contactItems;

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

        public int getContactLimit() {
            return contactLimit;
        }

        public void setContactLimit(int contactLimit) {
            this.contactLimit = contactLimit;
        }

        public Object getProduItemAdditionVOs() {
            return produItemAdditionVOs;
        }

        public void setProduItemAdditionVOs(Object produItemAdditionVOs) {
            this.produItemAdditionVOs = produItemAdditionVOs;
        }

        public List<ContactItemsBean> getContactItems() {
            return contactItems;
        }

        public void setContactItems(List<ContactItemsBean> contactItems) {
            this.contactItems = contactItems;
        }

        public static class ContactItemsBean {
            private String description;
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
}

