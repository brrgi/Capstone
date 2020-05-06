package com.example.msg.Domain;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
TODO: 프로덕트 ID도 돌려주도록 만들기, 이미지도 처리하도록 만들기, 에러 코드 등록.
 */

public class UserProductApi {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static int dummyCounter = 0; //더미를 만드는데 사용되는 카운터.
    private static  int errorCode = 1;
    /*
    에러코드입니다. Api에 함수를 새로 정의할 때는, 반드시 함수가 정상 동작했을 경우, errorCode를 1로 되돌려주는 작업을 해주십시오.
    0: 어떠한 이유로 코드가 끝까지 동작하지 못함.
    1: 최근에 동작한 모든 코드가 정상 동작함.
    2:
    */

    public static int getErrorCode() {
        return errorCode;
    }


    public static UserProductModel makeDummy() {

        UserProductModel userProductModel = new UserProductModel(null ,"0000", "핑크솔트", null, "핑크솔트입니다.", "향신료"
                , "소금", 3, "300g", "2020-05-03", false
        , 2.5, 2.5 );

        switch(dummyCounter % 4) {
            case 0:
                break;
            case 1:
                userProductModel.title = "참치";
                userProductModel.p_description = "방금 낚아올린 참치입니다.";
                userProductModel.categoryBig = "생선";
                userProductModel.categorySmall = "참치";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                break;
            case 2:
                userProductModel.title = "대구";
                userProductModel.p_description = "방금 낚아올린 대구입니다.";
                userProductModel.categoryBig = "생선";
                userProductModel.categorySmall = "대구";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                break;
            default:
                userProductModel.title = "설탕";
                userProductModel.p_description = "달콤한 설탕입니다.";
                userProductModel.categoryBig = "향신료";
                userProductModel.categorySmall = "설탕";
                userProductModel.latitude = 5.0;
                userProductModel.longitude = 5.0;
                break;
        }

        UserProductApi.dummyCounter++;
        return userProductModel;
    }
    /*
    입력 : 없음
    출력 : 더미 UserProductModel
    설명 : 간단한 더미 모델을 제공하는 함수입니다. 테스트 용도로 사용하십시오.
     */

    public static void postProduct(UserProductModel userProductModel) {
        db.collection("UserProducts").add(userProductModel);
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
        errorCode = 0; //작업 시작.

        final ArrayList<UserProductModel> modelList = new ArrayList<UserProductModel>();
        final double finalCurLongitude = curLongitude;
        final double finalRange = range;


        try {
            db.collection("UserProducts").
                    whereGreaterThan("latitude", curLatitude - range).whereLessThan("latitude", curLatitude + range)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            UserProductModel userProductModel = null;

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userProductModel = document.toObject(UserProductModel.class);
                                   if((userProductModel.longitude > finalCurLongitude - finalRange) && (userProductModel.longitude < finalCurLongitude + finalRange)){
                                        modelList.add(userProductModel);
                                   }
                                }
                                errorCode = 1; //모든 코드가 정상동작함.

                            } else {
                                //
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Test", "Erorr: ",e);
                }
            });
        } catch(Exception e) {
            Log.d("Test", e.toString());

        }

        //Log.d("test", modelList.get(0).title);
        return modelList;
    }
    /*
    입력: 현재 경도와 위도, 반경.
    출력: UserProductModel의 검색된 어레이리스트
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
