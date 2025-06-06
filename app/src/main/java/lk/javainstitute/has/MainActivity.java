package lk.javainstitute.has;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //variables
    Animation topAnim, bottomAinm;
    ImageView imageView;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAinm = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        imageView = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        imageView.setAnimation(topAnim);
        logo.setAnimation(bottomAinm);
        slogan.setAnimation(bottomAinm);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}