package com.enhanced.endgameodyssey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void loadImage(String mImageName, ImageView mImageIcon){
        int resID = this.getResources().getIdentifier(mImageName , "drawable", this.getPackageName());
        if(resID!=0) {//The associated resource identifier. Returns 0 if no such resource was found. (0 is not a valid resource ID.)
            mImageIcon.setImageResource(resID);
        }
    }
}
