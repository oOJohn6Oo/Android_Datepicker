package com.john6.datepicker.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.john6.datepicker.R;

import java.util.List;

public class DatePickerRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int itemLayout;
    //当前年月日
    private int currentDay;
    private int currentYear;
    private int currentMonth;

    private List<Integer> dateList;
    private OnItemClickListener onItemClickListener;

    //用户选择的年月日
    private int selectPosition = -1;
    private int selectYear = -1;
    private int selectMonth = -1;

    //请求显示的年月
    private int requestMonth = -1;
    private int requestYear = -1;

    //自定义颜色
    private int color_bgd_selected_day;
    private int color_bgd_before_today;
    private int color_bgd_after_today;
    private int color_bgd_today;
    private int color_font_selected_day;
    private int color_font_before_today;
    private int color_font_after_today;
    private int color_font_today;


    public DatePickerRecycleViewAdapter(Context context, int itemLayout, int currentDay, int currentMonth, int currentYear, List<Integer> dateList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.currentDay = currentDay;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.dateList = dateList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateHolder(LayoutInflater.from(context).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DateHolder viewHolder = (DateHolder) holder;
        int thisDay = dateList.get(holder.getAdapterPosition());
        if (thisDay > 0) {
            viewHolder.date.setText(String.valueOf(thisDay));       //设置日期 注意要转成String
            //今天以前
            if (requestYear < currentYear || (requestYear == currentYear && requestMonth < currentMonth) ||
                    (requestYear == currentYear && requestMonth == currentMonth && thisDay < currentDay)) {
                viewHolder.date.setTextColor(color_font_before_today==0?Color.LTGRAY:color_font_before_today);
                viewHolder.date.setBackgroundColor(color_bgd_before_today==0?Color.WHITE:color_bgd_before_today);
                viewHolder.date.setOnClickListener(null);
            } else {
                //今天
                if (thisDay == currentDay && requestMonth == currentMonth && requestYear == currentYear) {
                    viewHolder.date.setText(R.string.date_picker_today);
                    if(color_font_today!=0){
                        viewHolder.date.setTextColor(color_font_today);
                    }
                    viewHolder.date.setBackgroundColor(color_bgd_today==0?Color.WHITE:color_bgd_today);
                //今天以后
                }else {
                    if(color_font_after_today!=0){
                        viewHolder.date.setTextColor(color_font_after_today);
                    }
                    viewHolder.date.setBackgroundColor(color_bgd_after_today==0?Color.WHITE:color_bgd_after_today);
                }
                //点击后选中,改变选中的位置（日）、月、年 ===》更新视图
                viewHolder.date.setOnClickListener(view -> {
                    this.selectPosition = holder.getAdapterPosition();
                    this.selectYear = this.requestYear;
                    this.selectMonth = this.requestMonth;

                    if (onItemClickListener != null) {
                        onItemClickListener.OnClick(holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();
                });
                //检测到是选中的日期更改样式,并且不响应点击事件
                if (holder.getAdapterPosition() == selectPosition && selectMonth == requestMonth && selectYear == requestYear) {
                    viewHolder.date.setBackgroundResource(R.drawable.selector_btn_date_selected);

                    if(color_bgd_selected_day!=0) {
                        ((GradientDrawable) viewHolder.date.getBackground()).setColor(color_bgd_selected_day);
                    }
                    viewHolder.date.setTextColor(color_font_selected_day==0?Color.WHITE:color_font_selected_day);
                    viewHolder.date.setOnClickListener(null);
                }
            }

        } else {
            viewHolder.date.setBackgroundColor(color_bgd_before_today==0?Color.WHITE:color_bgd_before_today);
            ((DateHolder) holder).date.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return dateList == null ? 0 : dateList.size();
    }

    public int getRequestMonth() {
        return requestMonth;
    }

    public void setRequestMonth(int requestMonth) {
        this.requestMonth = requestMonth;
    }

    public int getRequestYear() {
        return requestYear;
    }

    public void setRequestYear(int requestYear) {
        this.requestYear = requestYear;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setColor_bgd_selected_day(int color_bgd_selected_day) {
        this.color_bgd_selected_day = color_bgd_selected_day;
    }

    public void setColor_bgd_before_today(int color_bgd_before_today) {
        this.color_bgd_before_today = color_bgd_before_today;
    }

    public void setColor_bgd_after_today(int color_bgd_after_today) {
        this.color_bgd_after_today = color_bgd_after_today;
    }

    public void setColor_bgd_today(int color_bgd_today) {
        this.color_bgd_today = color_bgd_today;
    }

    public void setColor_font_selected_day(int color_font_selected_day) {
        this.color_font_selected_day = color_font_selected_day;
    }

    public void setColor_font_before_today(int color_font_before_today) {
        this.color_font_before_today = color_font_before_today;
    }

    public void setColor_font_after_today(int color_font_after_today) {
        this.color_font_after_today = color_font_after_today;
    }

    public void setColor_font_today(int color_font_today) {
        this.color_font_today = color_font_today;
    }

    public interface OnItemClickListener {
        void OnClick(int position);
    }


    class DateHolder extends RecyclerView.ViewHolder {
        TextView date;
        DateHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.btn_date_item_date);
        }
    }
}
