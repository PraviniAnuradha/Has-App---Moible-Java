package lk.javainstitute.has;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import lk.javainstitute.has.modal.User;
import lk.javainstitute.has.service.UserService;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getName();
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private SharedPreferences preferences;
    private ImageView imageView;
    private Uri uri;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView2);



        String email = preferences.getString("email", "");
        if (!email.isEmpty()) {
            fireStore.collection("user").whereEqualTo("email", email)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isComplete()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    user = snapshot.toObject(User.class);

                                    storage.getReference("profile/"+user.getAccNo())
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Picasso.get().load(uri).fit().into(imageView);
                                                }
                                            });

                                    TextView accNo = view.findViewById(R.id.textView21);
                                    accNo.setText(user.getAccNo());
                                    EditText name = view.findViewById(R.id.editTextText3);
                                    name.setText(user.getFullName());
                                    EditText email = view.findViewById(R.id.editTextTextEmailAddress);
                                    email.setText(user.getEmail());
                                    EditText mobile = view.findViewById(R.id.editTextPhone);
                                    mobile.setText(user.getMobile());
                                    EditText nic = view.findViewById(R.id.editTextText2);
                                    nic.setText(user.getNic());
                                    EditText address = view.findViewById(R.id.editTextText4);
                                    address.setText(user.getAddress());
                                    EditText dob = view.findViewById(R.id.editTextDate);
                                    dob.setText(user.getDob());

                                }
                            }
                        }
                    });
        }

        view.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Profile");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Cam");

                uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


                resultLauncher.launch(intent);


            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nic = view.findViewById(R.id.editTextText2);
                EditText fullName = view.findViewById(R.id.editTextText3);
                EditText address = view.findViewById(R.id.editTextText4);
                EditText dob = view.findViewById(R.id.editTextDate);
                EditText email = view.findViewById(R.id.editTextTextEmailAddress);
                EditText mobile = view.findViewById(R.id.editTextPhone);

                User user1 = new User();
                user1.setFullName(String.valueOf(fullName.getText()));
                user1.setNic(String.valueOf(nic.getText()));
                user1.setAddress(String.valueOf(address.getText()));
                user1.setDob(String.valueOf(dob.getText()));
                user1.setEmail(String.valueOf(email.getText()));
                user1.setMobile(String.valueOf(mobile.getText()));

                new UserService(getContext()).update(user1);
            }
        });
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Picasso.get().load(uri).fit().into(imageView);
                        storage.getReference("profile").child(user.getAccNo())
                                .putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Try again later", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG,e.getMessage());
                                    }
                                });
                    }
                }
            }
    );
}