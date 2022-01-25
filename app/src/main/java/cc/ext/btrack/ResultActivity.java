
package cc.ext.btrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ext.btrack.R;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class ResultActivity extends AppCompatActivity {

    GifImageView gifImageView;
    TextView bmiTextView, typeTextView, risksTextView;

    private void settingUpGif() {

        int[] res = new int[]{R.drawable.gif1, R.drawable.gif2, R.drawable.gif3, R.drawable.gif4, R.drawable.gif5,
                R.drawable.gif6, R.drawable.gif7, R.drawable.gif8, R.drawable.gif9, R.drawable.gif10, R.drawable.gif11,
                R.drawable.gif12, R.drawable.gif13, R.drawable.gif14};

        int idx = new Random().nextInt(res.length);
        int random = (res[idx]);
        gifImageView.setImageResource(random);
    }

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String condition = "";
        String risk = "";
        String color = "";
        // Getting Intent
        Intent intent = getIntent();
        float BMI = intent.getFloatExtra("BMI", 0);
        float height = intent.getFloatExtra("Height", 0);
        float weight = intent.getFloatExtra("Weight", 0);
        String[] types = new String[]{"Very severely underweight", "Severely underweight", "Underweight",
                "Healthy", "Overweight", "Moderately obese", "Severely obese", "Very severely obese"
        };
        String[] colorTypes = new String[]{"#D3A174", "#DA7C28", "#D36706", "#388E3C", "#E37777", "#E83535", "#B12323", "#700D0D"};
        String[] risks = new String[]{"Risk of developing problems such as nutritional deficiency and osteoporosis",
                "Low Risk", "Moderate risk of developing heart disease, high blood pressure, stroke, diabetes"
                , "High risk of developing heart disease, high blood pressure, stroke, diabetes"};

        settingUpWidgets();
        settingUpGif();
        bmiTextView.setText(String.valueOf(BMI));

        if (BMI < 15) {
            condition = types[0];
            risk = risks[0];
            color = colorTypes[0];
        } else if (BMI < 16) {
            condition = types[1];
            risk = risks[0];
            color = colorTypes[1];
        } else if (BMI < 18.5) {
            condition = types[2];
            color = colorTypes[2];
            risk = risks[0];
        } else if (BMI < 25) {
            condition = types[3];
            color = colorTypes[3];
            risk = risks[1];
        } else if (BMI < 30) {
            condition = types[4];
            color = colorTypes[4];
            risk = risks[2];
        } else if (BMI < 35) {
            condition = types[5];
            color = colorTypes[5];
            risk = risks[2];
        } else if (BMI < 40) {
            condition = types[6];
            color = colorTypes[6];
            risk = risks[3];
        } else if (BMI > 40) {
            condition = types[7];
            color = colorTypes[7];
            risk = risks[3];
        }

        typeTextView.setText(condition);
        typeTextView.setTextColor(Color.parseColor(color));
        risksTextView.setText(risk);
    }

    private void settingUpWidgets() {
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        typeTextView = (TextView) findViewById(R.id.typeTextView);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        risksTextView = (TextView) findViewById(R.id.healthRisksTextView);
    }

    public void goDashboard(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


}