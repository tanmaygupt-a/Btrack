package cc.ext.btrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.ext.btrack.R;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    static MainActivity activityA;
    ImageButton btn_add;
    SharedPreferences sharedPreferences;
    TextView goalTextView, weightTextView, leftTextView, dateTextView, bmiTextView, textViewbtrack;
    ImageView stats, lineDetails;
    DecimalFormat df;
    String dateFull;

    public static MainActivity getInstance() {
        return activityA;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityA = this;
        initializingWidgets();

        // Setting up fade to exclude bottom navigation bar from animation
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);


        sharedPreferences = this.getSharedPreferences("com.ext.btrack", MODE_PRIVATE);
        String leftMssg = "";
        df = new DecimalFormat("###.##");
        float numWeight = 0;
        if (sharedPreferences.getString("weight", "") != "") {
            numWeight = Float.parseFloat(sharedPreferences.getString("weight", "--"));
            numWeight = Float.parseFloat(df.format(numWeight));
            weightTextView.setText(String.valueOf(numWeight));
        }
        goalTextView.setText(sharedPreferences.getString("goal", "--"));

        if (sharedPreferences.getString("weight", "") != "") {
            float weight = Float.parseFloat(sharedPreferences.getString("weight", ""));
            float goal = Float.parseFloat(sharedPreferences.getString("goal", ""));
            float leftWeight = Float.parseFloat(df.format(goal - weight));
            if (leftWeight > 0) {
                leftMssg = "+ " + leftWeight + " kg remaining";
            } else leftMssg = leftWeight + " kg remaining";

        }
        leftTextView.setText(leftMssg);


        // setting date
        String dd = sharedPreferences.getString("date", null);
        if (dd != null) {
            String[] keydate = dd.split("/");
            String month = getMonthForInt(Integer.parseInt(keydate[1]));
            Log.d("TAG", "saved month: " + month);
            dateFull = month + " " + keydate[2] + ", " + keydate[0];
        }
        dateTextView.setText(dateFull);
        bmiTextView.setText(sharedPreferences.getString("bmi", "--"));
    }

    private void initializingWidgets() {
        weightTextView = (TextView) findViewById(R.id.weightTextView);
        goalTextView = (TextView) findViewById(R.id.goalTextView);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        leftTextView = (TextView) findViewById(R.id.leftTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        bmiTextView = (TextView) findViewById(R.id.bmiiTextView);
        textViewbtrack = (TextView) findViewById(R.id.btrackTextView);
        stats = (ImageView) findViewById(R.id.imageView5);
        lineDetails = (ImageView) findViewById(R.id.imageView6);
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 12) {
            month = months[num - 1];
        }
        return month;
    }

    public void addNew(View view) {
        Intent intent = new Intent(getApplicationContext(), addBMI.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create(textViewbtrack, "textviewone"),
                Pair.create(lineDetails, "buttoncalculate"));
        startActivity(intent, optionsCompat.toBundle());
    }
}