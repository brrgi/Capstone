/*
데이터베이스의 실제 항목과 1:1 대응되는 모델입니다. 데이터베이스와 직접적으로 연결되기에 생산성을 위해서 모든 변수들이 public 형태를 가지고 있고
복잡한 기능을 따로 정의하지 않습니다. 이 모델을 직접적으로 다루는 복잡한 기능들은 Domain 패키지의 Api 항목에 정의되어있습니다.
 */

package com.example.msg.DatabaseModel;

public class UserProductModel {
    public String user_id = null;
    public String title = null;
    public String p_imageURL = null;
    public String p_description = null;
    public String categoryBig = null;
    public String categorySmall = null;
    public int quality = -1;
    public String quantity = null;
    public String expiration_date = null;
    public boolean completed = false;
    public double latitude = -1;
    public double longitude = -1;
    public String uproduct_id = null;

    public UserProductModel() {
        //파이어스토어의 정상 동작을 위해 필요한 생성자.
    }

    public UserProductModel( String uproduct_id, String user_id, String title, String p_imageURL, String p_description, String categoryBig, String categorySmall, int quality, String quantity, String expiration_date, boolean completed, double latitude, double longitude) {
        this.uproduct_id = uproduct_id;
        this.user_id = user_id;
        this.title = title;
        this.p_imageURL = p_imageURL;
        this.p_description = p_description;
        this.categoryBig = categoryBig;
        this.categorySmall = categorySmall;
        this.quality = quality;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.completed = completed;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    //필요한 모든 변수를 취하는 생성자입니다.


}
