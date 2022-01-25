package cc.ext.btrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ext.btrack.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addBMI extends AppCompatActivity {
    final float ft = (float) 0.0328084;
    final float lb = (float) 2.205;
    ImageView maleCard, femaleCard, maleLogo, femaleLogo;
    TextView maleText, femaleText, cmText, ftText, lbText, kgText;
    EditText heightEditText, weightEditText, goalEditText, ageEditText;
    String gender, height_unit, weight_unit = "";
    ImageButton submitBtn;
    SharedPreferences sharedPreferences;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_b_m_i);

        initializingWidgets();
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        df = new DecimalFormat("###.##");
        sharedPreferences = getApplicationContext().getSharedPreferences("com.ext.btrack", MODE_PRIVATE);
        setInitialValues();

        /**
         *  CARD On Click Listeners
         */
        maleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateManCard();
                deactivateFemaleCard();
            }
        });
        femaleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateFemaleCare();
                deactivateManCard();
            }
        });


        /**
         * Height on Click Listener
         */

        cmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftText.setTextColor(Color.parseColor("#486A7A"));
                cmText.setTextColor(Color.parseColor("#FBFBFB"));

                if (!heightEditText.getText().toString().isEmpty()) {

                    float height_ft = Float.parseFloat(heightEditText.getText().toString());
                    int height_cm = Math.round(height_ft / ft);

                    heightEditText.setText(String.valueOf(height_cm));
                }


                height_unit = "cm";
            }
        });

        ftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmText.setTextColor(Color.parseColor("#486A7A"));
                ftText.setTextColor(Color.parseColor("#FBFBFB"));
                if (!heightEditText.getText().toString().isEmpty()) {

                    float height_cm = Float.parseFloat(heightEditText.getText().toString());
                    float height_ft = Float.parseFloat(df.format(height_cm * ft));
                    heightEditText.setText(String.valueOf(height_ft));
                }

                height_unit = "ft";
            }
        });


        /**
         * Weight On CLick Listener
         */

        kgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbText.setTextColor(Color.parseColor("#486A7A"));
                kgText.setTextColor(Color.parseColor("#FBFBFB"));

                if (!weightEditText.getText().toString().isEmpty()) {
                    float weight_lb = Float.parseFloat(weightEditText.getText().toString());

                    float weight_kg = Float.parseFloat(df.format(weight_lb / lb));

                    weightEditText.setText(String.valueOf(weight_kg));
                }
                weight_unit = "kg";
            }
        });

        lbText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kgText.setTextColor(Color.parseColor("#486A7A"));
                lbText.setTextColor(Color.parseColor("#FBFBFB"));

                if (!weightEditText.getText().toString().isEmpty()) {
                    float weight_kg = Float.parseFloat(weightEditText.getText().toString());
                    float weight_lb = Float.parseFloat(df.format(weight_kg * lb));

                    weightEditText.setText(String.valueOf(weight_lb));
                }
                weight_unit = "lb";
            }
        });


        /**
         * Final submit Button Listener
         */

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float heightFinal = Float.parseFloat(heightEditText.getText().toString());
                if (height_unit == "ft") {
                    heightFinal = heightFinal / ft;
                }
                float height_fin = heightFinal;
                heightFinal = heightFinal / 100;

                float weight_final = Float.parseFloat(weightEditText.getText().toString());
                if (weight_unit == "lb") {
                    weight_final = weight_final / lb;
                }

                String finalGender = gender;
                float goal = Float.parseFloat(goalEditText.getText().toString());
                int age = Integer.parseInt(ageEditText.getText().toString());

                float heightSq = heightFinal * heightFinal;
                float BMI = Float.parseFloat(df.format(weight_final / heightSq));

                if((weight_final == 0 || heightFinal ==0) || (goal == 0 || age == 0)){
                    Toast.makeText(addBMI.this, "Please enter all the values!", Toast.LENGTH_SHORT).show();

                }else {

                    /**
                     * Saving data in shared preeferences
                     */
                    sharedPreferences.edit().putString("height", String.valueOf(height_fin)).apply();
                    sharedPreferences.edit().putString("weight", String.valueOf(weight_final)).apply();
                    sharedPreferences.edit().putString("goal", String.valueOf(goal)).apply();
                    sharedPreferences.edit().putString("age", String.valueOf(age)).apply();
                    Log.d("this", "data from shared pref: weight : " + sharedPreferences.getString("weight", "--"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    String dd = dateFormat.format(date);
                    Log.d("Date", "Date is : " + dd);
                    sharedPreferences.edit().putString("date", dd).apply();
                    sharedPreferences.edit().putString("bmi", String.valueOf(BMI)).apply();
                    MainActivity.getInstance().finish();

                    /**
                     *  Navigating to result Activity
                     */
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("BMI", BMI);
                    intent.putExtra("Height", heightFinal * 100);
                    intent.putExtra("Weight", weight_final);
                    startActivity(intent);
                    finish();
                    // Toast.makeText(addBMI.this, "BMI is " + BMI, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    /*
    Initializing Widgets
     */
    private void initializingWidgets() {

        maleCard = (ImageView) findViewById(R.id.male_imageview);
        femaleCard = (ImageView) findViewById(R.id.female_imageview);
        maleLogo = (ImageView) findViewById(R.id.maleLogo);
        femaleLogo = (ImageView) findViewById(R.id.femaleLogo);
        maleText = (TextView) findViewById(R.id.maleText);
        femaleText = (TextView) findViewById(R.id.femaleText);
        cmText = (TextView) findViewById(R.id.cmText);
        ftText = (TextView) findViewById(R.id.ftText);
        lbText = (TextView) findViewById(R.id.lbText);
        kgText = (TextView) findViewById(R.id.kgText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        goalEditText = (EditText) findViewById(R.id.goalEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);
        submitBtn = (ImageButton) findViewById(R.id.calcButton);
    }

    private void activateManCard() {
        maleCard.setImageResource(R.drawable.selected_card);
        maleLogo.setImageResource(R.drawable.male_selected);
        maleText.setTextColor(Color.parseColor("#FBFBFB"));
        gender = "M";

    }

    private void deactivateManCard() {
        maleCard.setImageResource(R.drawable.unselected_card);
        maleLogo.setImageResource(R.drawable.male_unselected);
        maleText.setTextColor(Color.parseColor("#486A7A"));
    }

    private void activateFemaleCare() {
        femaleCard.setImageResource(R.drawable.selected_card);
        femaleLogo.setImageResource(R.drawable.female_selected);
        femaleText.setTextColor(Color.parseColor("#FBFBFB"));
        gender = "F";
    }

    private void deactivateFemaleCard() {
        femaleCard.setImageResource(R.drawable.unselected_card);
        femaleLogo.setImageResource(R.drawable.female_unselected);
        femaleText.setTextColor(Color.parseColor("#486A7A"));
    }

    private void setInitialValues() {
        heightEditText.setText(sharedPreferences.getString("height", "0"));
        weightEditText.setText(sharedPreferences.getString("weight", "0"));
        ageEditText.setText(sharedPreferences.getString("age", "0"));
        goalEditText.setText(sharedPreferences.getString("goal", "0"));
    }

}