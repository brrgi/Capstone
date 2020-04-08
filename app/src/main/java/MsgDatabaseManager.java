import androidx.annotation.NonNull;

public class MsgDatabaseManager {

}

/*
    final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
@Override
public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
        while(!imageUrl.isComplete());

        UserModel userModel = new UserModel();
        userModel.userName = name.getText().toString();
        userModel.profileImageUrl=imageUrl.getResult().toString();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

        */