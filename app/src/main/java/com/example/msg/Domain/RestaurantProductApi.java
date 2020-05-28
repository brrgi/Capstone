package com.example.msg.Domain;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.example.msg.DatabaseModel.RestaurantModel;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RestaurantProductApi {

    public interface MyCallback {
        void onSuccess(RestaurantProductModel restaurantProductModel);
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

    public static RestaurantProductModel makeDummy() {
        RestaurantProductModel restaurantProductModel = new RestaurantProductModel(null, "1234", "핑크솔트", null,"분홍색 소금입니다."
                ,"향신료", "소금", 1, "100g", null, 500, false
                ,100, -1, 5.0, 5.0);

        return restaurantProductModel;
    }
/*
    입력 : 없음
    출력 : 더미 ProductModel
    설명 : 간단한 더미 모델을 제공하는 함수입니다. 테스트 용도로 사용하십시오.
*/



    public static void postProduct(final RestaurantProductModel restaurantProductModel, final Uri imageUri, final MyCallback myCallback) {
        db.collection("ResProducts").add(restaurantProductModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("ResProducts").document(documentReference.getId())
                                .update("rproduct_id", documentReference.getId());
                        restaurantProductModel.rproduct_id = documentReference.getId();
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
    출력: 없음.
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
                        "completed", restaurantProductModel.completed
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
    출력: 없음
    동작: ProductModel을 받아서 해당 객체에 대응되는 데이터베이스 자리에 값을 업데이트합니다. 성공할 경우 콜백 함수를 통해서 성공한 객체를
    그대로 반환하며, 실패할 경우는 onFail 콜백함수를 부릅니다.
     */


    public static void getProductList(final double curLatitude, final double curLongitude, final double range, final MyListCallback myCallback) {
        db.collection("ResProducts").
                whereGreaterThan("latitude", curLatitude - range).whereLessThan("latitude", curLatitude + range)
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
                                if((restaurantProductModel.longitude > curLongitude - range) && (restaurantProductModel.longitude < curLongitude + range)){
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
    출력: 없음.
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
    출력: 없음.
    동작: 특정 uid와 특정 completed 상태를 가지는 모델들을 가져옵니다. 그 결과는 myCallback의 onSuccess 안에서 참조할 수 있습니다.
     */

    public static ArrayList<RestaurantProductModel> filterByCategory(ArrayList<RestaurantProductModel> modelList, String categoryBig, String categorySmall) {
        ArrayList<RestaurantProductModel> newModelList = new ArrayList<RestaurantProductModel>();
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

    public static ArrayList<RestaurantProductModel> filterByKeyWord(ArrayList<RestaurantProductModel> modelList, String keyword) {
        ArrayList<RestaurantProductModel> newModelList = new ArrayList<RestaurantProductModel>();
        RestaurantProductModel restaurantProductModel = null;

        for(int i =0; i< modelList.size(); i++) {
            restaurantProductModel = modelList.get(i);
            if(restaurantProductModel.title.contains(keyword) || restaurantProductModel.p_description.contains(keyword)
                    || restaurantProductModel.categoryBig.contains(keyword) || restaurantProductModel.categorySmall.contains(keyword)) {
                newModelList.add(restaurantProductModel);
            }
        }

        return newModelList;
    }
    /*
    입력: 모델 리스트와 키워드
    출력: 필터링된 모델 리스트
    동작: 모델 리스트에서 키워드와 매칭되는 모델만 필터링해서 반환합니다. 얕은 복사를 일으키므로 주의하십시오.
     */

    public static void sortByDistance(ArrayList<RestaurantProductModel> modelList, final double curLatitude, final double curLongitude) {
        Comparator<RestaurantProductModel> myComparator = new Comparator<RestaurantProductModel>() {
            @Override
            public int compare(RestaurantProductModel o1, RestaurantProductModel o2) {
                double distance2 = Math.abs(curLatitude - o2.latitude) + Math.abs(curLongitude - o2.longitude);
                double distance1 = Math.abs(curLatitude - o1.latitude) + Math.abs(curLongitude - o1.longitude);
                //성능 향상을 위해서 맨허튼 거리 계산법 사용.

                return (int)(distance1 - distance2);
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 현재 위치의 GPS값과 userProductModel List
    출력: 없음
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
    출력: 없음.
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

    public static void sortBySubscription(ArrayList<RestaurantProductModel> modelList, ArrayList<SubscriptionModel> subscriptionList) {
        for(int i=0; i <modelList.size(); i++) {
            for(int j = 0; j < subscriptionList.size(); j++) {

                if(modelList.get(i).res_id.equals(subscriptionList.get(j).res_id)) {
                    Log.d("1234", modelList.get(i).title);
                    modelList.get(i).fast = true;
                    Log.d("1234", Boolean.toString(modelList.get(i).fast));
                }
            }
        }

        sortByFast(modelList);
    }
    //TODO: 동작 방식에 문제가 있음. 다시 코딩하는 것을 권장.




}
