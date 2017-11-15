package com.e.model.pay;

public class Freight {
  private String freight_id;
  private String province;
  private String flow_place;
  private Double first_weight;
  private Double continue_weight;
  private String express_company_id;

  public String getFreight_id() {
    return freight_id;
  }

  public void setFreight_id(String freight_id) {
    this.freight_id = freight_id;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getFlow_place() {
    return flow_place;
  }

  public void setFlow_place(String flow_place) {
    this.flow_place = flow_place;
  }

  public Double getFirst_weight() {
    return first_weight;
  }

  public void setFirst_weight(Double first_weight) {
    this.first_weight = first_weight;
  }

  public Double getContinue_weight() {
    return continue_weight;
  }

  public void setContinue_weight(Double continue_weight) {
    this.continue_weight = continue_weight;
  }

  public String getExpress_company_id() {
    return express_company_id;
  }

  public void setExpress_company_id(String express_company_id) {
    this.express_company_id = express_company_id;
  }
}
