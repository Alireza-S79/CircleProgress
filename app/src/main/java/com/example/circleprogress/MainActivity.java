package com.example.circleprogress;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    CircleProgress circleProgress;
    SeekBar seekBar;
    CheckBox cbAutocolored , cbShowPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleProgress = findViewById(R.id.circle_progress);
        seekBar = findViewById(R.id.seekbar_progress);
        cbAutocolored = findViewById(R.id.cb_auto_colored);
        cbAutocolored.setChecked(circleProgress.isAutoColord());

        cbShowPercent = findViewById(R.id.cb_show_percent);
        cbShowPercent.setChecked(circleProgress.isShowPercent());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    circleProgress.setProgressWithAnimation(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cbAutocolored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
            circleProgress.setAutoColord(checked);
            }
        });

        cbShowPercent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                circleProgress.setShowPercent(checked);
            }
        });

    }
}
