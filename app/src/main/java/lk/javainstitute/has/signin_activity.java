package lk.javainstitute.has;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.javainstitute.has.modal.User;
import lk.javainstitute.has.service.UserService;
import lk.javainstitute.has.util.AccNumbGenerator;


public class signin_activity extends AppCompatActivity {
    private static final String TAG = signin_activity.class.getName();

    private FirebaseFirestore fireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);

        fireStore = FirebaseFirestore.getInstance();


        Button button2 = findViewById(R.id.alreadyhavelogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signin_activity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText fn = findViewById(R.id.signUpfn);
                TextInputEditText ln = findViewById(R.id.signUpun);
                TextInputEditText email = findViewById(R.id.signUpemail);
                TextInputEditText mobile = findViewById(R.id.signUpmobile);
                TextInputEditText password = findViewById(R.id.signUppassword);

                int accNum = AccNumbGenerator.getAccNumber();

                User user = new User(
                        String.valueOf(fn.getText()),
                        String.valueOf(ln.getText()),
                        String.valueOf(email.getText()),
                        String.valueOf(mobile.getText()),
                        String.valueOf(password.getText()),
                        String.valueOf(accNum),
                        0,
                        null,
                        null,
                        null
                );
                new UserService(signin_activity.this).add(user);
            }
        });

    }
}