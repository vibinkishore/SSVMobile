package com.viki.ssvmobile;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {


    ImageView imageView;
    public static int  splashout=1250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadeinanim);
        imageView.startAnimation(animation);

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {

                       startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                       finish();

                   }
                   },splashout);




    }
}
