package lk.javainstitute.has.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lk.javainstitute.has.dto.FundTransfer;
import lk.javainstitute.has.modal.Transaction;
import lk.javainstitute.has.modal.User;

public class FundTransferService {
    private static final String TAG = FundTransferService.class.getName();
    private FirebaseFirestore fireStore;
    private Context context;
    private SharedPreferences preferences;

    public FundTransferService(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        fireStore = FirebaseFirestore.getInstance();
    }
    public void transfer(FundTransfer transfer){
        String userEmail = preferences.getString("email","");
        if(!userEmail.isEmpty()){
            fireStore.collection("user").whereEqualTo("email",userEmail)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isComplete()){
                                if(!task.getResult().isEmpty()){
                                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                                        User user = snapshot.toObject(User.class);
                                        if(user.getAmount()>= transfer.getAmount()){
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                            String dateTime = formatter.format(LocalDateTime.now());

                                            fireStore.collection("transaction").orderBy("id", Query.Direction.DESCENDING)
                                                    .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isComplete()){
                                                                int nextId = 1;
                                                                if(!task.getResult().isEmpty()){
                                                                    for (QueryDocumentSnapshot snapshot1 : task.getResult()){
                                                                        int lastId = Integer.parseInt(snapshot1.get("id").toString());
                                                                        nextId = lastId+1;
                                                                    }
                                                                }
                                                                Transaction transaction = new Transaction(
                                                                        nextId,
                                                                        user.getAccNo(),
                                                                        transfer.getAccNo(),
                                                                        transfer.getAmount(),
                                                                        dateTime,
                                                                        transfer.getDes(),
                                                                        transfer.getBank()
                                                                );
                                                                doTransfer(user, transaction);
                                                            }
                                                        }
                                                    });


                                        }else {
                                            Toast.makeText(context, "You Don't have enough money", Toast.LENGTH_SHORT).show();
                                        }
                                        
                                    }
                                }
                            }
                        }
                    });

        }
    }
    private void doTransfer(User user, Transaction transaction){
        fireStore.collection("transaction").add(transaction)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG,"Transaction was success");
                        updateAmount(user, transaction.getTo(), transaction.getAmount());
                        Toast.makeText(context, "Transaction was success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                });
    }
    private void updateAmount(User user, String to, double amount){
        fireStore.collection("user").whereEqualTo("accNo",user.getAccNo())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            double currentAmount = user.getAmount();
                            double newAmount = currentAmount - amount;
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                snapshot.getReference().update("amount",newAmount)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i(TAG,"User FROM amount updated");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, e.getMessage());
                                            }
                                        });
                            }
                        }


                    }
                });
        fireStore.collection("user").whereEqualTo("accNo",to)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){

                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                User user1 = snapshot.toObject(User.class);
                                double currentAmount = user1.getAmount();
                                double newAmount = currentAmount + amount;
                                snapshot.getReference().update("amount",newAmount)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i(TAG,"User TO amount updated");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, e.getMessage());
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}
