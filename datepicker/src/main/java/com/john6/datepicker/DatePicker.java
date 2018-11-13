package com.john6.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.john6.datepicker.adapter.DatePickerRecycleViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatePicker extends Dialog {

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 0;

    public static final int DATE_SHOW_STYLE_1 = 0;
    public static final int DATE_SHOW_STYLE_2 = 1;
    public static final int DATE_SHOW_STYLE_3 = 2;

    private Context context;
    private TextView tv_confirm;
    private TextView tv_cancel;
    private TextView tv_today;
    private TextView tv_year_month;
    private ImageView go_left;
    private ImageView go_right;
    private View datePickerView;

    private TextView tv_sunday;
    private TextView tv_monday;
    private TextView tv_tuesday;
    private TextView tv_wednesday;
    private TextView tv_thursday;
    private TextView tv_friday;
    private TextView tv_saturday;

    private View.OnClickListener onCancelCLickListener;
    private View.OnClickListener onConfirmCLickListener;

    private DatePickerRecycleViewAdapter myAdapter;
    private int currentYear;           //年
    private int currentMonth;          //月
    private int currentDay;     //日
    private int dayCount;       //总天数
    private int firstWeekday;   //第一天周几
    private List<Integer> dateList;

    private int selectedYear = -1;
    private int selectedMonth = -1;
    private int selectedDay = -1;

    /*
        自定义颜色
     */
    private int color_cancel;
    private int color_confirm;
    private int color_now_date;
    private int color_left_arrow;
    private int color_right_arrow;
    private int color_year_month;
    private int color_week;
    private int[] color_weeks = new int[7];
    private int color_bgd_selected_day;
    private int color_bgd_before_today;
    private int color_bgd_after_today;
    private int color_bgd_today;
    private int color_font_selected_day;
    private int color_font_before_today;
    private int color_font_after_today;
    private int color_font_today;

    private int date_show_style;
    Calendar now = Calendar.getInstance();

    /**
     * @param styleId 自定义的bottom样式
     */
    public DatePicker(Context context, int styleId) {
        super(context, styleId);
        this.context = context;
    }

    public DatePicker(Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initColor();

        this.setContentView(datePickerView);

        if (getWindow() == null) {
            return;
        }
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void initView() {
        datePickerView = LayoutInflater.from(context).inflate(R.layout.date_picker, null);

        tv_confirm = datePickerView.findViewById(R.id.date_picker_confirm);
        tv_cancel = datePickerView.findViewById(R.id.date_picker_cancel);
        tv_today = datePickerView.findViewById(R.id.date_picker_today);
        tv_year_month = datePickerView.findViewById(R.id.date_picker_year_month);
        go_left = datePickerView.findViewById(R.id.date_picker_left_arrow);
        go_right = datePickerView.findViewById(R.id.date_picker_right_arrow);
        RecyclerView recyclerView = datePickerView.findViewById(R.id.date_picker_recycler_view);

        tv_sunday = datePickerView.findViewById(R.id.date_picker_sunday);
        tv_monday = datePickerView.findViewById(R.id.date_picker_monday);
        tv_tuesday = datePickerView.findViewById(R.id.date_picker_tuesday);
        tv_wednesday = datePickerView.findViewById(R.id.date_picker_wednesday);
        tv_thursday = datePickerView.findViewById(R.id.date_picker_thursday);
        tv_friday = datePickerView.findViewById(R.id.date_picker_friday);
        tv_saturday = datePickerView.findViewById(R.id.date_picker_saturday);

        dateList = new ArrayList<>();
        currentYear = now.get(Calendar.YEAR);          //获取年份
        currentMonth = now.get(Calendar.MONTH) + 1;      //获取月份（范围0-11所以+1）
        currentDay = now.get(Calendar.DAY_OF_MONTH);   //获取日期
        initData();
        setYearMonth(currentYear,currentMonth);
        myAdapter = new DatePickerRecycleViewAdapter(context, R.layout.item_btn_date, currentDay, currentMonth, currentYear, dateList);
        myAdapter.setRequestYear(currentYear);
        myAdapter.setRequestMonth(currentMonth);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        recyclerView.setAdapter(myAdapter);

         /*
          “取消”的单击事件
         */
        tv_cancel.setOnClickListener(view -> {
            if (onCancelCLickListener != null) {
                onCancelCLickListener.onClick(view);
            }
        });



        /*
          “确认”的单击事件
         */
        tv_confirm.setOnClickListener(view -> {
            // 用户当前页未选择任何Item，并且之前也没有选择
            if (myAdapter.getSelectPosition() == -1 && selectedDay == -1) {
                Toast.makeText(context, "未选择任何时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (onConfirmCLickListener != null) {
                onConfirmCLickListener.onClick(view);
            }
        });


        /*
            "左箭头"的单击事件
         */
        go_left.setOnClickListener(view -> {
            if (now.get(Calendar.MONTH) == 0) {
                now.roll(Calendar.YEAR, -1);
            }
            now.roll(Calendar.MONTH, -1);
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            setYearMonth(year,month);
            myAdapter.setRequestYear(year);
            myAdapter.setRequestMonth(month);
            initData();
            myAdapter.notifyDataSetChanged();
        });

        /*
            "右箭头"的单击事件
         */
        go_right.setOnClickListener(view -> {
            if (now.get(Calendar.MONTH) == 11) {
                now.roll(Calendar.YEAR, 1);
            }
            now.roll(Calendar.MONTH, 1);
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            setYearMonth(year, month);
            myAdapter.setRequestYear(year);
            myAdapter.setRequestMonth(month);
            initData();
            myAdapter.notifyDataSetChanged();
        });

        /*
            列表选择事件
         */
        myAdapter.setOnItemClickListener(position -> {
            selectedMonth = myAdapter.getRequestMonth();
            selectedYear = myAdapter.getRequestYear();
            selectedDay = dateList.get(myAdapter.getSelectPosition());
            switch (date_show_style) {
                case DATE_SHOW_STYLE_2:
                    tv_today.setText(context.getString(R.string.date_selected_day_style_2, selectedYear, selectedMonth, selectedDay));
                    break;
                case DATE_SHOW_STYLE_3:
                    tv_today.setText(context.getString(R.string.date_selected_day_style_3, selectedYear, selectedMonth, selectedDay));
                    break;
                default:
                    tv_today.setText(context.getString(R.string.date_selected_day_style_1, selectedYear, selectedMonth, selectedDay));
                    break;
            }
            myAdapter.notifyDataSetChanged();
        });
    }

    private void setYearMonth(int year, int month) {
        switch (month) {
            case 1:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_january)));
                break;
            case 2:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_february)));
                break;
            case 3:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_march)));
                break;
            case 4:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_april)));
                break;
            case 5:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_may)));
                break;
            case 6:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_june)));
                break;
            case 7:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_july)));
                break;
            case 8:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_august)));
                break;
            case 9:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_september)));
                break;
            case 10:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_october)));
                break;
            case 11:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_november)));
                break;
            default:
                tv_year_month.setText(context.getString(R.string.date_picker_year_month, year, context.getString(R.string.date_picker_december)));
                break;
        }
    }

    private void initColor() {
        if (color_cancel != 0) {
            tv_cancel.setTextColor(color_cancel);
        }
        if (color_confirm != 0) {
            tv_confirm.setTextColor(color_confirm);
        }
        if (color_now_date != 0) {
            tv_today.setTextColor(color_now_date);
        }
        if (color_left_arrow != 0) {
            go_left.setBackgroundColor(color_left_arrow);
        }
        if (color_right_arrow != 0) {
            go_right.setBackgroundColor(color_right_arrow);
        }
        if (color_year_month != 0) {
            tv_year_month.setTextColor(color_year_month);
        }

        initWeekColor();

        if (color_bgd_selected_day != 0) {
            myAdapter.setColor_bgd_selected_day(color_bgd_selected_day);
        }
        if (color_bgd_before_today != 0) {
            myAdapter.setColor_bgd_before_today(color_bgd_before_today);
        }
        if (color_bgd_after_today != 0) {
            myAdapter.setColor_bgd_after_today(color_bgd_after_today);
        }
        if (color_bgd_today != 0) {
            myAdapter.setColor_bgd_today(color_bgd_today);
        }
        if (color_font_selected_day != 0) {
            myAdapter.setColor_font_selected_day(color_font_selected_day);
        }
        if (color_font_before_today != 0) {
            myAdapter.setColor_font_before_today(color_font_before_today);
        }
        if (color_font_after_today != 0) {
            myAdapter.setColor_font_after_today(color_font_after_today);
        }
        if (color_font_today != 0) {
            myAdapter.setColor_font_today(color_font_today);
        }
    }

    /**
     * 1. 设置了整体颜色，未设置单个颜色
     * 2. 设置了单个颜色，未设置整体颜色
     * 3. 设置了整体颜色，设置了单个颜色
     * <p>
     * 优先级 单个>整体，先整体设置一遍，再单个设置一遍
     * *注意不为0才生效**
     **/
    private void initWeekColor() {
        List<TextView> weeks = new ArrayList<>();
        weeks.add(tv_sunday);
        weeks.add(tv_monday);
        weeks.add(tv_tuesday);
        weeks.add(tv_wednesday);
        weeks.add(tv_thursday);
        weeks.add(tv_friday);
        weeks.add(tv_saturday);
        if (color_week != 0) {
            for (TextView day : weeks) {
                day.setTextColor(color_week);
            }
        }
        for (int i = 0; i < 7; i++) {
            if (color_weeks[i] != 0)
                weeks.get(i).setTextColor(color_weeks[i]);
        }
    }

    private void initData() {
        now.set(Calendar.DATE, 1);                      //设置为当月第一天
        firstWeekday = now.get(Calendar.DAY_OF_WEEK);  //获取第一天星期几,假如是2，就是星期一
        now.roll(Calendar.DATE, -1);            //这个月第一天倒退一天
        dayCount = now.get(Calendar.DATE);              //获取这个月总天数


        dateList.clear();
        for (int i = 1; i < firstWeekday; i++) {            //填入-1
            dateList.add(-1);
        }
        for (int i = 0; i < dayCount; i++) {        //填入1,2,3,4,...
            dateList.add(i + 1);
        }
        while (dateList.size() <= 35) {                //防止弹上弹下，始终显示6行
            dateList.add(-1);
        }
    }



    public DatePicker setColorCancel(int colorCancel) {
        this.color_cancel = colorCancel;
        return this;
    }


    public DatePicker setColorConfirm(int colorConfirm) {
        this.color_confirm = colorConfirm;
        return this;
    }


    public DatePicker setColorNowDate(int colorNowDate) {
        this.color_now_date = colorNowDate;
        return this;
    }

    public DatePicker setColorLeftArrow(int colorLeftArrow) {
        this.color_left_arrow = colorLeftArrow;
        return this;
    }

    public DatePicker setColorRightArrow(int colorRightArrow) {
        this.color_right_arrow = colorRightArrow;
        return this;
    }

    public DatePicker setColorYearMonth(int colorYearMonth) {
        this.color_year_month = colorYearMonth;
        return this;
    }

    public DatePicker setColorWeek(int color_week) {
        this.color_week = color_week;
        return this;
    }

    public DatePicker setColorWeeks(int dayOfSeven, int color) {
        color_weeks[dayOfSeven] = color;
        return this;
    }

    public DatePicker setColorBgdSelectedDay(int colorBgdSelectedDay) {
        this.color_bgd_selected_day = colorBgdSelectedDay;
        return this;
    }

/*    public DatePicker setColorBgdBeforeToday(int colorBgdBeforeToday) {
        this.color_bgd_before_today = colorBgdBeforeToday;
        return this;
    }

    public DatePicker setColorBgdAfterToday(int colorBgdAfterToday) {
        this.color_bgd_after_today = colorBgdAfterToday;
        return this;
    }*/

    /**
     * 设置“今天”的背景颜色
     *
     * @param colorBgdToday 背景颜色
     * @return 当前对象
     */
    public DatePicker setColorBgdToday(int colorBgdToday) {
        this.color_bgd_today = colorBgdToday;
        return this;
    }

    /**
     * 设置已选择日期的文字颜色
     *
     * @param colorFontSelectedDay 选择日期的文字颜色
     * @return 当前对象
     */
    public DatePicker setColorFontSelectedDay(int colorFontSelectedDay) {
        this.color_font_selected_day = colorFontSelectedDay;
        return this;
    }

    /**
     * 设置“今天”以前日期的文字颜色
     *
     * @param colorFontBeforeToday ”今天“以前的文字颜色
     * @return 当前对象
     */
    public DatePicker setColorFontBeforeToday(int colorFontBeforeToday) {
        this.color_font_before_today = colorFontBeforeToday;
        return this;
    }

    /**
     * 设置“今天”以后日期的文字颜色
     *
     * @param colorFontAfterToday ”今天“以后的文字颜色
     * @return 当前对象
     */
    public DatePicker setColorFontAfterToday(int colorFontAfterToday) {
        this.color_font_after_today = colorFontAfterToday;
        return this;
    }

    /**
     * 设置“今天”的文字颜色
     *
     * @param colorFontToday ”今天“的文字颜色
     * @return 当前对象
     */
    public DatePicker setColorFontToday(int colorFontToday) {
        this.color_font_today = colorFontToday;
        return this;
    }

    /**
     * 设置日期显示样式风格
     *
     * @param dateShowStyle 显示风格
     * @return 当前对象
     */
    public DatePicker setDateShowStyle(int dateShowStyle) {
        this.date_show_style = dateShowStyle;
        return this;
    }

    /**
     * @return 选择的年份
     */
    public int getSelectedYear() {
        return selectedYear;
    }

    /**
     * @return 选择的月份
     */
    public int getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * @return 选择的日期
     */
    public int getSelectedDay() {
        return selectedDay;
    }

    public int[] getSelectDate() {
        return new int[]{getSelectedYear(),getSelectedMonth(),getSelectedDay()};
    }

    public DatePicker setOnCancelClickListener(View.OnClickListener onCancelCLickListener) {
        this.onCancelCLickListener = onCancelCLickListener;
        return this;
    }

    public DatePicker setOnConfirmClickListener(View.OnClickListener onConfirmCLickListener) {
        this.onConfirmCLickListener = onConfirmCLickListener;
        return this;
    }
}
