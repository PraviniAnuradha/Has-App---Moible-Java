package lk.javainstitute.has.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import lk.javainstitute.has.HomeActivity;
import lk.javainstitute.has.LoginActivity;
import lk.javainstitute.has.R;
import lk.javainstitute.has.modal.User;

public class UserService {
    private static final String TAG = UserService.class.getName();
    private FirebaseFirestore fireStore;
    private SharedPreferences preferences;
    private Context context;
    private NotificationManager notificationManager;
    public UserService(Context context){
        this.context = context;
        this.fireStore = FirebaseFirestore.getInstance();
        preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    public void add(User user){
        fireStore.collection("user").whereEqualTo("email",user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            if(task.getResult().isEmpty()){
                                fireStore.collection("user").add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show();
                                                showNotification(user.getFullName());
                                                context.startActivity(new Intent(context, LoginActivity.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG,e.getMessage());
                                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else {
                                Toast.makeText(context, "Already have a account from this email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void showNotification(String name){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "success",
                    "Success",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setShowBadge(true);
            notificationChannel.setDescription("Welcome");
            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new NotificationCompat.Builder(context.getApplicationContext(),"success")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Hello "+name)
                    .setContentText("Welcome to the most existing digital wallet experience as a Has user.")
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(1,notification);
        }
    }

    public void userAuth(String username, String password){
        fireStore.collection("user").where(Filter.and(
                Filter.equalTo("username",username),
                Filter.equalTo("password",password)
        )).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    if(!task.getResult().isEmpty()){
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            User user = snapshot.toObject(User.class);
                            preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email",user.getEmail());
                            if(editor.commit()){
                                context.startActivity(new Intent(context, HomeActivity.class));
                            }else {
                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void update(User user){
        String email = preferences.getString("email","");
        if(!email.isEmpty()){
            fireStore.collection("user").whereEqualTo("email",email)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isComplete()){
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("fullName",user.getFullName());
                                    map.put("email",user.getEmail());
                                    map.put("address",user.getAddress());
                                    map.put("dob",user.getDob());
                                    map.put("mobile",user.getMobile());
                                    map.put("nic",user.getNic());
                                    snapshot.getReference().update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
        }else {
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context,LoginActivity.class));
        }

    }
}
