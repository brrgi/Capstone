

package com.example.msg.Api;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.FoodModel;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.example.msg.DatabaseModel.RestaurantProductModel;
import com.example.msg.DatabaseModel.SubscriptionModel;
import com.example.msg.DatabaseModel.UserProductModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/*
파일명: RestaurantProductApi
설  명: 식당이 등록한 상품에 관련된 기능들을 모아놓은 클래스입니다. 대부분 기능들이 콜백을 이용해서 RestaurantProductModel 이라는 객체를 이용해서 비동기적으로 동작합니다.
파이어베이스에 관한 사전지식이 없으면 대부분의 코드가 읽기 어려우나, 밖에서는 협업자가 콜백 구조를 안다면 내부 구현들을 신경쓰지 않고 활용할수 있도록 만들었습니다.
모든 데이터베이스 관련 동작은 onSuccess를 통해서 정보를 돌려주고, onFail을 통해서 실패 결과를 알려줍니다.
 */
public class RestaurantProductApi {

    public interface MyCallback {
        void onSuccess(RestaurantProductModel restaurantProductModel);
        void onFail(int errorCode, Exception e);
    }
    public interface MyFilterCallback {
        void onSuccess(FoodModel foodModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<RestaurantProductModel> restaurantModelArrayList);
        void onFail(int errorCode, Exception e);
    }
    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();



    public static void postProduct(final RestaurantProductModel restaurantProductModel, final Uri imageUri, final MyCallback myCallback) {
        db.collection("ResProducts").add(restaurantProductModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("ResProducts").document(documentReference.getId())
                                .update("rproduct_id", documentReference.getId());
                        restaurantProductModel.rproduct_id = documentReference.getId();
                        Log.d("resTest", restaurantProductModel.rproduct_id);
                        postImage(restaurantProductModel, imageUri, myCallback);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: RestaurantProductModel
    동작: RestaurantProductModel을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 product_id를 가진 상태)
     */

    public static void postProductWithNoImage(final RestaurantProductModel restaurantProductModel,final MyCallback myCallback) {
        db.collection("ResProducts").add(restaurantProductModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("ResProducts").document(documentReference.getId())
                                .update("rproduct_id", documentReference.getId());
                        restaurantProductModel.rproduct_id = documentReference.getId();
                        Log.d("resTest", restaurantProductModel.rproduct_id);
                        myCallback.onSuccess(restaurantProductModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: RestaurantProductModel
    출력: 없음.
    동작: RestaurantProductModel을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 product_id를 가진 상태)
     */

    public static void postImage(final RestaurantProductModel restaurantProductModel, final Uri imageUri, final MyCallback myCallback){
        final String directory = "ResProducts/";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference imageReference = storageReference.child( directory + restaurantProductModel.rproduct_id);

        UploadTask uploadTask = imageReference.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(10 ,e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    return imageReference.getDownloadUrl();

                } else {
                    return null;
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                if(downloadUri != null) {
                    restaurantProductModel.p_imageURL = task.getResult().toString();
                    db.collection("ResProducts").document(restaurantProductModel.rproduct_id)
                            .update("p_imageURL", restaurantProductModel.p_imageURL);
                    //사진을 올린 직후에 서버 스토리지의 사진 링크를 데이터베이스에 업데이트함.
                    myCallback.onSuccess(restaurantProductModel);
                } else {
                    myCallback.onFail(11, null);
                }

            }
        });

    }
    /*
    입력: 모델과 콜백함수.
    동작: 모델을 받아서 모델의 image를 storage에 등록합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 image 변수를 얻은 상태)
     */

    public static void getProduct(String productId, final MyCallback myCallback) {
        db.collection("ResProducts").document(productId).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        RestaurantProductModel temp = document.toObject(RestaurantProductModel.class);
                                        myCallback.onSuccess(temp);
                                    } else {
                                        myCallback.onFail(2, null);
                                        //document no search;
                                    }
                                } else {
                                    myCallback.onFail(1, null);
                                    //exception of firestore
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });

    }
    /*
    입력: 프로덕트ID
    출력: 프로덕트ID에 대응되는 ResProduct 모델.
    동작: 프로덕트 ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델을 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다.
     */

    public static void updateProduct(final RestaurantProductModel restaurantProductModel, final MyCallback myCallback) {
        db.collection("ResProducts").document(restaurantProductModel.rproduct_id).
                update(
                        "res_id", restaurantProductModel.res_id,
                        "title", restaurantProductModel.title,
                        "p_imageURL", restaurantProductModel.p_imageURL,
                        "p_description", restaurantProductModel.p_description,
                        "categoryBig", restaurantProductModel.categoryBig,
                        "categorySmall", restaurantProductModel.categorySmall,
                        "quality", restaurantProductModel.quality,
                        "quantity", restaurantProductModel.quantity,
                        "expiration_date", restaurantProductModel.expiration_date,
                        "cost", restaurantProductModel.cost,
                        "fast", restaurantProductModel.fast,
                        "stock", restaurantProductModel.stock,
                        "completed", restaurantProductModel.completed,
                        "saleDateYear", restaurantProductModel.saleDateYear,
                        "saleDateMonth", restaurantProductModel.saleDateMonth,
                        "saleDateDay", restaurantProductModel.saleDateDay
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(restaurantProductModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });

    }
    /*
    입력: ProductModel
    동작: ProductModel을 받아서 해당 객체에 대응되는 데이터베이스 자리에 값을 업데이트합니다. 성공할 경우 콜백 함수를 통해서 성공한 객체를
    그대로 반환하며, 실패할 경우는 onFail 콜백함수를 부릅니다.
     */


    public static void getProductList(final double curLatitude, final double curLongitude, final int range, final MyListCallback myCallback) {
        final double latitudeRange = DistanceApi.meterToLatitude(range);
        final double longitudeRange = DistanceApi.meterToLongitude(range);

        db.collection("ResProducts").
                whereGreaterThan("latitude", curLatitude - latitudeRange).whereLessThan("latitude", curLatitude + latitudeRange)
                .whereEqualTo("completed", -1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;
                        ArrayList<RestaurantProductModel> restaurantProductModelArrayList = new ArrayList<RestaurantProductModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                if((restaurantProductModel.longitude > curLongitude - longitudeRange) && (restaurantProductModel.longitude < curLongitude + longitudeRange)){
                                    if(restaurantProductModel.stock>0)
                                        restaurantProductModelArrayList.add(restaurantProductModel);
                                }
                            }
                            myCallback.onSuccess(restaurantProductModelArrayList);

                        } else {
                            myCallback.onFail(1, null);
                            //태스크 실패.
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e); //실패.
            }
        });
    }
    /*
    입력: 현재 경도와 위도, 반경.
    동작: 현재 위치로부터 일정 반경 내에 등록된 Product를 모두 가져옵니다. 그 결과는 myCallback의 onSuccess 안에서 참조할 수 있습니다.
     */

    public static void getProductListById(final String id, final int completed , final MyListCallback myCallback) {
        db.collection("ResProducts")
                .whereEqualTo("res_id", id)
                .whereEqualTo("completed", completed)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RestaurantProductModel restaurantProductModel = null;
                        ArrayList<RestaurantProductModel> restaurantProductModelArrayList = new ArrayList<RestaurantProductModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                restaurantProductModel = document.toObject(RestaurantProductModel.class);
                                restaurantProductModelArrayList.add(restaurantProductModel);
                            }
                            myCallback.onSuccess(restaurantProductModelArrayList);

                        } else {
                            myCallback.onFail(1, null);
                            //태스크 실패.
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e); //실패.
            }
        });
    }
    /*
    입력: uid와 completed 상태.
    동작: 특정 uid와 특정 completed 상태를 가지는 모델들을 가져옵니다. 그 결과는 myCallback의 onSuccess 안에서 참조할 수 있습니다.
     */

    public static ArrayList<RestaurantProductModel> filterByCategory(ArrayList<RestaurantProductModel> modelList, String categorySmall) {
        ArrayList<RestaurantProductModel> newModelList = new ArrayList<RestaurantProductModel>();
        for(int i = 0; i < modelList.size(); i++) {
            if(modelList.get(i).categorySmall.equals(categorySmall)){
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

    public static void keywordSend(final String keyword, final MyFilterCallback myFilterCallback) {
        db.collection("Foods").whereEqualTo("food_name",keyword).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FoodModel foodModel = new FoodModel();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                foodModel = documentSnapshot.toObject(FoodModel.class);
                                Log.d("FoodModel",foodModel.ingredients.get(0));
                            }
                            myFilterCallback.onSuccess(foodModel);
                        } else {
                            myFilterCallback.onFail(0, null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myFilterCallback.onFail(1,e);
            }
        });
    }

    public static ArrayList<RestaurantProductModel> filterByKeyWord(ArrayList<RestaurantProductModel> modelList, FoodModel foodModel) {
        ArrayList<RestaurantProductModel> newModelList = new ArrayList<RestaurantProductModel>();
        RestaurantProductModel restaurantProductModel = null;

        if(foodModel.ingredients == null) return newModelList;

        for(int j = 0; j<foodModel.ingredients.size()-1; j++) {
            String keyword = foodModel.ingredients.get(j);
            for(int i =0; i< modelList.size(); i++) {
                restaurantProductModel = modelList.get(i);
                if(restaurantProductModel.title.contains(keyword) || restaurantProductModel.p_description.contains(keyword)
                        || restaurantProductModel.categoryBig.contains(keyword) || restaurantProductModel.categorySmall.contains(keyword)) {
                    newModelList.add(restaurantProductModel);
                }
            }
        }
        return newModelList;
    }
    /*
    입력: 모델 리스트와 키워드
    출력: 필터링된 모델 리스트
    동작: 모델 리스트에서 키워드와 매칭되는 모델만 필터링해서 반환합니다. 얕은 복사를 일으키므로 주의하십시오.
     */

    public static ArrayList<RestaurantProductModel> filterByKeyWord2(final ArrayList<RestaurantProductModel> modelList, String keyword) {
        ArrayList<RestaurantProductModel> newModelList = new ArrayList<>();
        RestaurantProductModel restaurantProductModel;
        for(int i =0; i< modelList.size(); i++) {
            restaurantProductModel = modelList.get(i);
            if(restaurantProductModel.title.contains(keyword) || restaurantProductModel.p_description.contains(keyword)
                    || restaurantProductModel.categoryBig.contains(keyword) || restaurantProductModel.categorySmall.contains(keyword)) {
                newModelList.add(restaurantProductModel);
            }
        }
        return newModelList;
    }

    public static void sortByDistance(ArrayList<RestaurantProductModel> modelList, final double curLatitude, final double curLongitude) {
        Comparator<RestaurantProductModel> myComparator = new Comparator<RestaurantProductModel>() {
            @Override
            public int compare(RestaurantProductModel o1, RestaurantProductModel o2) {
                double distance2 = Math.abs(curLatitude - o2.latitude) + Math.abs(curLongitude - o2.longitude);
                double distance1 = Math.abs(curLatitude - o1.latitude) + Math.abs(curLongitude - o1.longitude);
                //성능 향상을 위해서 맨허튼 거리 계산법 사용.
                if(distance1 > distance2) return 1;
                else return -1;
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 현재 위치의 GPS값과 userProductModel List
    동작 : 입력으로 들어온 Model의 리스트를 맨허튼 거리 계산법에 따라 가까운 순으로 정렬해줍니다.
     */


    public static void sortByPrice(ArrayList<RestaurantProductModel> modelList) {
        Comparator<RestaurantProductModel> myComparator = new Comparator<RestaurantProductModel>() {
            @Override
            public int compare(RestaurantProductModel o1, RestaurantProductModel o2) {
                return o1.cost - o2.cost;
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 모델의 어레이 리스트.
    출력: 없음.
    동작: 입력으로 들어온 모델의 리스트를 가격이 낮은 순으로 정렬해줍니다.
     */

    public static void sortByStock(ArrayList<RestaurantProductModel> modelList) {
        Comparator<RestaurantProductModel> myComparator = new Comparator<RestaurantProductModel>() {
            @Override
            public int compare(RestaurantProductModel o1, RestaurantProductModel o2) {
                return o2.stock - o1.stock;
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 모델의 어레이 리스트.
    동작: 입력으로 들어온 모델의 리스트를 재고가 많은 순으로 정렬해줍니다.
     */

    public static void sortByFast(ArrayList<RestaurantProductModel> modelList) {
        Comparator<RestaurantProductModel> myComparator = new Comparator<RestaurantProductModel>() {
            @Override
            public int compare(RestaurantProductModel o1, RestaurantProductModel o2) {
                if(o2.fast == true && o1.fast == false) {
                    return 1;
                } else if(o1.fast == true && o2.fast == false) {
                    return -1;
                } else{
                    return 0;
                }
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 모델의 어레이 리스트
    동작: 입력으로 들어온 모델의 리스트 중, 급처가 켜져있는 리스트를 앞으로 불러옵니다.
     */

    public static ArrayList<RestaurantProductModel> filterBySubscription(ArrayList<RestaurantProductModel> modelList, ArrayList<SubscriptionModel> subscriptionList) {

        ArrayList<RestaurantProductModel> restaurantProductModels = new ArrayList<>();

        for(int i=0; i <modelList.size(); i++) {
            for(int j = 0; j < subscriptionList.size(); j++) {
                if(modelList.get(i).res_id.equals(subscriptionList.get(j).res_id)) {
                    restaurantProductModels.add(modelList.get(i));
                }
            }
        }

        return restaurantProductModels;
    }
    /*
    입력: 상품의 모델 리스트와, 구독 모델의 리스트.
    동작: 상품의 모델 리스트 중, 구독과 관련있는 상품들만 걸러내서 반환합니다.
     */

    public static void deleteDuplicated(ArrayList<RestaurantProductModel> reference, ArrayList<RestaurantProductModel> target) {
        for(int i =0; i < reference.size(); i++) {
            for(int j =0; j < target.size(); j++) {
                if(reference.get(i).rproduct_id.equals(target.get(j).rproduct_id)) {
                    target.remove(j);
                    break;
                }
            }
        }
    }
    /*
    입력: 참조 모델과 타겟 모델
    동작: 타겟 모델의 요소 중 참조 모델안에 있는 요소들과 중복되는 것들을 모조리 지웁니다.
     */



    public static ArrayList<RestaurantProductModel> filterByDistance(ArrayList<RestaurantProductModel> inputModels, double curLatitude, double curLongitude, int range) {
        double rangeLatitude = DistanceApi.meterToLatitude(range);
        double rangeLongitude = DistanceApi.meterToLongitude(range);

        ArrayList<RestaurantProductModel> outputModels = new ArrayList<>();
        for(int i =0; i < inputModels.size(); i ++) {
            if(Math.abs(inputModels.get(i).latitude - curLatitude) < rangeLatitude) {
                if(Math.abs(inputModels.get(i).longitude - curLongitude) < rangeLongitude) {
                    outputModels.add(inputModels.get(i));
                }
            }
        }
        return outputModels;
    }
    /*
    입력: 모델, 현재 위도, 경도, 반경
    출력 및 동작: 입력으로 받은 모델을 멘허튼 거리로 계산해서 일정 반경 미만만 걸러서 돌려줍니다.
     */

    public static ArrayList<RestaurantProductModel> filterByQuality(ArrayList<RestaurantProductModel> inputModels, boolean allowLow, boolean allowMid, boolean allowHigh) {
        ArrayList<RestaurantProductModel> outputModels = new ArrayList<>();
        for(int i =0; i <inputModels.size(); i++) {
            switch(inputModels.get(i).quality) {
                case 1:
                    if(allowLow) outputModels.add(inputModels.get(i));
                    break;
                case 2:
                    if(allowMid) outputModels.add(inputModels.get(i));
                    break;
                case 3:
                    if(allowHigh) outputModels.add(inputModels.get(i));
                    break;
            }
        }
        return outputModels;
    }
    /*
    입력: 모델, 품질 허락(Allow) 여부.
    출력 및 동작: 입력으로 받은 모델을 allow 값에 따라 필터링해서 돌려줍니다.
     */

    public static ArrayList<RestaurantProductModel> filterByPrice(ArrayList<RestaurantProductModel> inputModels, int price) {
        ArrayList<RestaurantProductModel> outputModels = new ArrayList<>();
        for(int i =0; i <inputModels.size(); i++) {
            if(inputModels.get(i).cost < price) {
                outputModels.add(inputModels.get(i));
            }
        }
        return outputModels;
    }
    /*
    입력: 모델, 가격
    출력 및 동작: 입력으로 받은 모델을 가격순으로 정렬해서 돌려줍니다.
     */

    public static void deleteProduct(final String id, final MyCallback myCallback) {
        db.collection("ResProducts").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            myCallback.onSuccess(null);
                        }
                        else {
                            myCallback.onFail(1, null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 삭제할 모델의 id, 콜백함수.
    동작: id와 관련된 모델을 삭제합니다. 성공이나 실패시 콜백함수를 호출합니다.
     */

    public static void extractSubscribedModel(final ArrayList<RestaurantProductModel> restaurantProductModels, final MyListCallback myCallback) {
        final ArrayList<RestaurantProductModel> extractedModel = new ArrayList<>();
        String myId = AuthenticationApi.getCurrentUid();

        SubscriptionApi.getSubscriptionListByUserId(myId, new SubscriptionApi.MyListCallback() {
            @Override
            public void onSuccess(ArrayList<SubscriptionModel> subscriptionModelArrayList) {
                extractedModel.addAll(filterBySubscription(restaurantProductModels, subscriptionModelArrayList));
                deleteDuplicated(extractedModel, restaurantProductModels);
                myCallback.onSuccess(extractedModel);
            }

            @Override
            public void onFail(int errorCode, Exception e) {
                myCallback.onFail(errorCode, e);
            }
        });
    }
    /*
    입력: 모델리스트와 콜백
    출력 및 동작: 입력으로 들어온 모델들 중, 현재 로그인 된 id로 구독 된 식당의 모델만을 추출(원본에서는 삭제/deleteDuplicated함수가 쓰임)해서 콜백함수를 통해서 돌려줍니다.
     */



}
