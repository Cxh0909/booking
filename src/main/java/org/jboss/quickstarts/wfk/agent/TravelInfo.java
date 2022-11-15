package org.jboss.quickstarts.wfk.agent;

/**
 * @author yu zhang
 */
public class TravelInfo {
    private CommodityType commodityType;

    private Long customerId;

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
