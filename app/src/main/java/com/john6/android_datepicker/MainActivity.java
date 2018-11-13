package com.john6.android_datepicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.john6.datepicker.DatePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.test);
        btn.setOnClickListener(view -> {
            DatePicker datePicker = new DatePicker(MainActivity.this);

            datePicker.setOnCancelClickListener(view1 -> datePicker.dismiss())
                    //“确定”的点击事件
                    .setOnConfirmClickListener(view12 -> {
                        Toast.makeText(MainActivity.this, datePicker.getSelectedYear()+" "+
                                datePicker.getSelectedMonth()+" "+
                                datePicker.getSelectedDay(), Toast.LENGTH_SHORT).show();
                        datePicker.dismiss();})
                    //“取消”、“确定”的文字颜色
                    .setColorConfirm(Color.RED)
                    .setColorCancel(Color.DKGRAY)

                    //选择“时间”的显示样式
                    .setDateShowStyle(DatePicker.DATE_SHOW_STYLE_3)

                    // 左右箭头颜色
                    .setColorLeftArrow(Color.RED)
                    .setColorRightArrow(Color.RED)

                    //星期的颜色
                    .setColorWeeks(DatePicker.SUNDAY,Color.RED)

                    //“今天”以前、今天、“今天”以后 的显示文字颜色
                    .setColorFontBeforeToday(Color.LTGRAY)
                    .setColorFontToday(Color.WHITE)
                    .setColorFontAfterToday(Color.BLACK)

                    //“今天”的背景颜色
                    .setColorBgdToday(Color.GREEN)
                    //选中日期的文字颜色和背景颜色
                    .setColorFontSelectedDay(Color.WHITE)
                    .setColorBgdSelectedDay(Color.RED)
                    .show();
        });

    }
}
