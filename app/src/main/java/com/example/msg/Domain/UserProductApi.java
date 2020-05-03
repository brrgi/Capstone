package com.example.msg.Domain;
import androidx.annotation.NonNull;

import com.example.msg.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserProductApi {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static int DummyCounter = 0; //더미를 만드는데 사용되는 카운터.

    //private int errorCode = 1; 에러 처리가 필요한경우. 주석을 풀고 사용.
    /*
    에러코드입니다. Api에 함수를 새로 정의할 때는, 반드시 함수가 정상 동작했을 경우, errorCode를 1로 되돌려주는 작업을 해주십시오.
    1: 최근에 동작한 모든 코드가 정상 동작함.


    public int getErrorCode() {
        return errorCode;
    }
*/

    public static UserProductModel makeDummy() {

        UserProductModel userProductModel = new UserProductModel("0000", "핑크솔트", null, "핑크솔트입니다.", "향신료"
                , "소금", 3, "300g", "2020-05-03", false
        , 2.5, 2.5 );

        switch(DummyCounter % 4) {
            case 0:
                return userProductModel;
            case 1:
                userProductModel.title = "참치";
                userProductModel.p_description = "방금 낚아올린 참치입니다.";
                userProductModel.categoryBig = "생선";
                userProductModel.categorySmall = "참치";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                return userProductModel;
            case 2:
                userProductModel.title = "대구";
                userProductModel.p_description = "방금 낚아올린 대구입니다.";
                userProductModel.categoryBig = "생선";
                userProductModel.categorySmall = "대구";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                return userProductModel;
            default:
                userProductModel.title = "설탕";
                userProductModel.p_description = "달콤한 설탕입니다.";
                userProductModel.categoryBig = "향신료";
                userProductModel.categorySmall = "설탕";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                return userProductModel;
        }
    }

    public static void postProduct(UserProductModel userProductModel) {
        db.collection("UserProducts").add(userProductModel);
        //TODO 이미지도 나중에 같이 넣어야함.
    }
    /*
    입력: UserProductModel.
    출력: 없음.
    동작: userProductModel을 받아서 데이터베이스에 추가합니다.
     */

    public static void updateProduct(UserProductModel userProductModel, String pid) {
        db.collection("UserProducts").document(pid).
                update("title", userProductModel.title,
                        "p_imageURL", userProductModel.p_imageURL,
                        "p_description", userProductModel.p_imageURL,
                        "categoryBig", userProductModel.categoryBig,
                        "categorySmall", userProductModel.categorySmall,
                        "quality", userProductModel.quality,
                        "quantity", userProductModel.quantity,
                        "expiration_date", userProductModel.expiration_date,
                        "completed", userProductModel.completed,
                        "latitude", userProductModel.latitude,
                        "longitude", userProductModel.longitude,
                        "user_id", userProductModel.user_id
                        );
    }
    /*
    입력: userProductModel, product Id
    출력: 없음
    동작: userProductModel을 받아서 해당 product id를 지니는 데이터베이스 자리에 값을 업데이트합니다.
     */


    public static ArrayList<UserProductModel> getProductList(double curLatitude, double curLongitude, double range) {
        final ArrayList<UserProductModel> modelList = new ArrayList<UserProductModel>();

        db.collection("UserProducts").
                whereGreaterThan("latitude", curLatitude - range).whereLessThan("latitude", curLatitude + range).
                whereGreaterThan("longitude", curLongitude - range).whereLessThan("longitude", curLongitude + range)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                modelList.add(document.toObject(UserProductModel.class));
                            }
                        } else {
                          //Eror code.
                        }
                    }
                });


        return modelList;
    }
    /*
    입력: 현재 경도와 위도, 반경.
    출력: UserProductModel의 어레이리스트
    동작: 현재 위치로부터 일정 반경 내에 등록된 UserProduct를 모두 가져옵니다.
     */

    public static ArrayList<UserProductModel> filterByCategory(ArrayList<UserProductModel> modelList, String categoryBig, String categorySmall) {
        ArrayList<UserProductModel> newModelList = new ArrayList<UserProductModel>();
        for(int i = 0; i < modelList.size(); i++) {
            if(modelList.get(i).categoryBig.equals(categoryBig) && modelList.get(i).categorySmall.equals(categorySmall)){
                newModelList.add(modelList.get(i));
            }
        }
        return newModelList;
    }
    /*
    입력: 모델 리스트와 카테고리
    출력: 필터링된 모델 리스트
    동작: 모델 리스트에서 입력 카테고리에 해당하는 모델만 필터링해서 반환합니다. 얕은 복사를 일으키므로 주의하십시오.
    */

    public static ArrayList<UserProductModel> filterByKeyWord(ArrayList<UserProductModel> modelList, String keyword) {
        ArrayList<UserProductModel> newModelList = new ArrayList<UserProductModel>();
        UserProductModel userProductModel = null;

        for(int i =0; i< modelList.size(); i++) {
            userProductModel = modelList.get(i);
            if(userProductModel.title.contains(keyword) || userProductModel.p_description.contains(keyword)
            || userProductModel.categoryBig.contains(keyword) || userProductModel.categorySmall.contains(keyword)) {
                newModelList.add(userProductModel);
            }
        }

        return newModelList;
    }
    /*
    입력: 모델 리스트와 키워드
    출력: 필터링된 모델 리스트
    동작: 모델 리스트에서 키워드와 매칭되는 모델만 필터링해서 반환합니다. 얕은 복사를 일으키므로 주의하십시오.
     */
}
