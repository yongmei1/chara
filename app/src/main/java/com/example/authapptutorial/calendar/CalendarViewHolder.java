package com.example.authapptutorial.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapptutorial.R;
import com.example.authapptutorial.calendar.CalendarAdaptor;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView dayOfMonth;
    public final CalendarAdaptor.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdaptor.OnItemListener onItemListener) {
        super(itemView);

        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
