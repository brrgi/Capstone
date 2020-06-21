package com.example.msg.Api;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.msg.DatabaseModel.*;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
TODO: 프로덕트 ID도 돌려주도록 만들기, 이미지도 처리하도록 만들기, 에러 코드 등록.
 */



public class UserProductApi {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public interface MyCallback {
        void onSuccess(UserProductModel userProductModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyFilterCallback {
        void onSuccess(FoodModel foodModel);
        void onFail(int errorCode, Exception e);
    }

    public interface MyListCallback{
        void onSuccess(ArrayList<UserProductModel> userProductModels);
        void onFail(int errorCode, Exception e);
    }
    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
    10: 파일 업로드 실패.
     */


    public static void postProduct(final UserProductModel userProductModel, final Uri imageUri, final MyCallback myCallback) {
        db.collection("UserProducts").add(userProductModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("UserProducts").document(documentReference.getId())
                                .update("uproduct_id", documentReference.getId());
                        userProductModel.uproduct_id = documentReference.getId();
                        postImage(userProductModel, imageUri, myCallback);
                        //이미지 포스트
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myCallback.onFail(0, e);
            }
        });
    }
    /*
    입력: 모델과 콜백함수.
    출력: 없음.
    동작: 모델을 받아서 데이터베이스에 추가합니다. 실패할경우 콜백함수 onFail을 호출합니다.
         성공할시에는 콜백함수 onSuccess를 호출하고, 성공한 객체를 돌려줍니다.(해당 객체는 product_id를 얻은 상태)
     */

    public static void postImage(final UserProductModel userProductModel, final Uri imageUri, final MyCallback myCallback){
        final String directory = "UserProducts/";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference imageReference = storageReference.child( directory + userProductModel.uproduct_id);

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
                    userProductModel.p_imageURL = task.getResult().toString();
                    db.collection("UserProducts").document(userProductModel.uproduct_id)
                            .update("p_imageURL", userProductModel.p_imageURL);
                    //사진을 올린 직후에 서버 스토리지의 사진 링크를 데이터베이스에 업데이트함.
                    myCallback.onSuccess(userProductModel);
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

    public static void updateProduct(final UserProductModel userProductModel, final MyCallback myCallback) {
        db.collection("UserProducts").document(userProductModel.uproduct_id).
                update(
                        "title", userProductModel.title,
                        "p_imageURL", userProductModel.p_imageURL,
                        "p_description", userProductModel.p_description,
                        "categoryBig", userProductModel.categoryBig,
                        "categorySmall", userProductModel.categorySmall,
                        "quality", userProductModel.quality,
                        "quantity", userProductModel.quantity,
                        "expiration_date", userProductModel.expiration_date,
                        "completed", userProductModel.completed,
                        "latitude", userProductModel.latitude,
                        "longitude", userProductModel.longitude,
                        "user_id", userProductModel.user_id

                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myCallback.onSuccess(userProductModel);
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

    public static void getProduct(String productId, final MyCallback myCallback) {
        db.collection("UserProducts").document(productId).get().
                addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        UserProductModel temp = document.toObject(UserProductModel.class);
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
    출력: 프로덕트ID에 대응되는 Product 모델.
    동작: 프로덕트 ID를 이용해서 데이터베이스에 서칭을 하고, 그 결과 나온 모델을 돌려줍니다. 콜백함수 onSuccess를 통해서 돌려줍니다.
     */

    public static void getProductList(final double curLatitude, final double curLongitude, final double range, final MyListCallback myCallback) {
        db.collection("UserProducts").
                whereGreaterThan("latitude", curLatitude - range).whereLessThan("latitude", curLatitude + range)
                .whereEqualTo("completed", -1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UserProductModel userProductModel = null;
                        ArrayList<UserProductModel> userProductModels = new ArrayList<UserProductModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userProductModel = document.toObject(UserProductModel.class);
                                if((userProductModel.longitude > curLongitude - range) && (userProductModel.longitude < curLongitude + range)){
                                    userProductModels.add(userProductModel);
                                }
                            }
                            myCallback.onSuccess(userProductModels);

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

    public static void getProductListById(final String id, int completed , final MyListCallback myCallback) {
        db.collection("UserProducts")
                .whereEqualTo("user_id", id)
                .whereEqualTo("completed", completed)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UserProductModel userProductModel = null;
                        ArrayList<UserProductModel> userProductModels = new ArrayList<UserProductModel>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userProductModel = document.toObject(UserProductModel.class);
                                userProductModels.add(userProductModel);
                            }
                            myCallback.onSuccess(userProductModels);

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
    입력: id와 completed 상태.
    출력: 없음.
    동작: id와 completed와 일치하는 Product를 모두 가져옵니다. 그 결과는 myCallback의 onSuccess 안에서 참조할 수 있습니다.
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

    public static void keywordSend(final String keyword, final MyFilterCallback myFilterCallback) {
        db.collection("Foods").whereEqualTo("food_name",keyword).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        FoodModel foodModel = new FoodModel();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                foodModel = documentSnapshot.toObject(FoodModel.class);
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


    public static ArrayList<UserProductModel> filterByKeyWord(final ArrayList<UserProductModel> modelList,final FoodModel foodModel) {
        ArrayList<UserProductModel> newModelList = new ArrayList<UserProductModel>();
        UserProductModel userProductModel;
        if(foodModel.ingredients.size()==0) {
            return newModelList;
        }
        else {
            for (int j = 0; j < foodModel.ingredients.size() - 1; j++) {
                String keyword = foodModel.ingredients.get(j);
                for (int i = 0; i < modelList.size(); i++) {
                    userProductModel = modelList.get(i);
                    if (userProductModel.title.contains(keyword) || userProductModel.p_description.contains(keyword)
                            || userProductModel.categoryBig.contains(keyword) || userProductModel.categorySmall.contains(keyword)) {
                        newModelList.add(userProductModel);
                    }
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


    public static ArrayList<UserProductModel> filterByKeyWord2(final ArrayList<UserProductModel> modelList,String keyword) {
        ArrayList<UserProductModel> newModelList = new ArrayList<UserProductModel>();
        UserProductModel userProductModel;
            for(int i =0; i< modelList.size(); i++) {
                userProductModel = modelList.get(i);
                if(userProductModel.title.contains(keyword) || userProductModel.p_description.contains(keyword)
                        || userProductModel.categoryBig.contains(keyword) || userProductModel.categorySmall.contains(keyword)) {
                    newModelList.add(userProductModel);
                }
            }
        return newModelList;
    }
    /* filterbykeyword2 : 음식이름기반 검색 이후 기본 재료 정렬
       return : userproductArraylist 담겨있는 newModelList
     */

    public static void sortByDistance(ArrayList<UserProductModel> modelList, double curLatitude, double curLongitude) {
        final double finalCurLatitude = curLatitude;
        final double finalCurLongitude = curLongitude;

        Comparator<UserProductModel> myComparator = new Comparator<UserProductModel>() {
            @Override
            public int compare(UserProductModel o1, UserProductModel o2) {
                double distance2 = Math.abs(finalCurLatitude - o2.latitude) + Math.abs(finalCurLongitude - o2.longitude);
                double distance1 = Math.abs(finalCurLatitude - o1.latitude) + Math.abs(finalCurLongitude - o1.longitude);
                //성능 향상을 위해서 맨허튼 거리 계산법 사용.

                return (int)(distance1 - distance2);
            }
        };

        Collections.sort(modelList, myComparator);
    }
    /*
    입력: 현재 위치의 GPS값과 userProductModel List
    출력: 없음
    동작 : 입력으로 들어온 UserProductModel의 리스트를 맨허튼 거리 계산법에 따라 가까운 순으로 정렬해줍니다.
     */



}
