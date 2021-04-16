package com.example.lesson_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lesson_10.R;
import com.example.lesson_10.model.weather.DailyForecast;
import com.example.lesson_10.util.Utils;

import java.util.List;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {
    private final List<DailyForecast> model;
    private final Context context;

    public MainRecyclerViewAdapter(Context context, List<DailyForecast> model) {
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_main_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.todayWeatherDesc.setText(model.get(position).getDay().getIconPhrase());
        holder.dayOfWeek.setText(Utils.getCurrentDay(context, model.get(position).getDate(), position));
        holder.temperatureHolder.setText(Utils.getTemperatureRange(model.get(position).getTemperature(), context));
        holder.icon.setImageResource(Utils.getWeatherIcon(model.get(position).getDay().getIcon()));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView todayWeatherDesc;
        private final TextView temperatureHolder;
        private final TextView dayOfWeek;
        private final ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todayWeatherDesc = itemView.findViewById(R.id.textView_listItem_weatherDesc);
            temperatureHolder = itemView.findViewById(R.id.textView_listItem_temperatureHolder);
            dayOfWeek = itemView.findViewById(R.id.textView_listItem_dayOfWeek);
            icon = itemView.findViewById(R.id.imageView_list_item_iconHolder);
        }
    }
}
