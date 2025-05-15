package lk.javainstitute.has.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.javainstitute.has.R;
import lk.javainstitute.has.modal.Transaction;
import lk.javainstitute.has.modal.User;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private FirebaseFirestore fireStore;
    private SharedPreferences preferences;
    private Context context;
    private ArrayList<Transaction> transactions;
    public TransactionHistoryAdapter(Context context){
        preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        fireStore = FirebaseFirestore.getInstance();
        String email = preferences.getString("email","");
        transactions = new ArrayList<>();
        if(!email.isEmpty()){
            this.context = context;
            fireStore.collection("user").whereEqualTo("email",email).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isComplete()){
                                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                                            User user = snapshot.toObject(User.class);
                                            fireStore.collection("transaction")
                                                    .whereEqualTo("from",user.getAccNo())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if(task.isComplete()){
                                                                for(QueryDocumentSnapshot snapshot1 : task.getResult()){
                                                                    Transaction transaction = snapshot1.toObject(Transaction.class);
                                                                    transactions.add(transaction);
                                                                }
                                                                TransactionHistoryAdapter.this.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    }


                                }
                            });
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fund_transfer_history_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getDate().setText(transactions.get(position).getDateTime());
        holder.getRef().setText(String.valueOf(transactions.get(position).getId()));
        holder.getAmount().setText(String.valueOf(transactions.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView category,ref,acc,date,amount,status;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.category = itemView.findViewById(R.id.textView15);
            this.ref = itemView.findViewById(R.id.textView16);
            this.date = itemView.findViewById(R.id.textView17);
            this.acc = itemView.findViewById(R.id.textView18);
            this.amount = itemView.findViewById(R.id.textView19);
            this.status = itemView.findViewById(R.id.textView20);
        }

        public TextView getCategory() {
            return category;
        }

        public TextView getRef() {
            return ref;
        }

        public TextView getAcc() {
            return acc;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getAmount() {
            return amount;
        }

        public TextView getStatus() {
            return status;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
