package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by LT on 2018-03-07.
 */

public class HomeItemBean {
    /**
     * item GUID
     */
    private String ItemGUID;
    /**
     * item name
     */
    private String Name;
    /**
     * item图片id
     */
    private int ImageId;
    /**
     * item 订单数量
     */
    private int OrderCount;

    public String getItemGUID() {
        return ItemGUID;
    }

    public void setItemGUID(String itemGUID) {
        ItemGUID = itemGUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }
}
