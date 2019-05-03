package info.project.hey.Class;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import info.project.hey.EditTaskAct;
import info.project.hey.R;

public class reclerAdapter extends RecyclerView.Adapter<reclerAdapter.MyHolderView> {
    private Context context;
    private ArrayList<MyEvent> myEvents;

    public reclerAdapter(Context context, ArrayList<MyEvent> myEvents){
        this.context = context;
        this.myEvents = myEvents;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event,viewGroup,false);
        return new reclerAdapter.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolderView myHolderView, int position) {
        myHolderView.judulevnt.setText(myEvents.get(position).getJudulevnt());
        myHolderView.ketevnt.setText(myEvents.get(position).getKetevnt());
        myHolderView.wktevnt.setText(myEvents.get(position).getWktevnt());
        myHolderView.tglevnt.setText(myEvents.get(position).getTglevnt());

        final String getJudulevnt = myEvents.get(position).getJudulevnt();
        final String getKetevnt = myEvents.get(position).getKetevnt();
        final String getWktevnt = myEvents.get(position).getWktevnt();
        final String getTglevnt = myEvents.get(position).getTglevnt();
        final String getKeyevnt = myEvents.get(position).getKeyevnt();

        myHolderView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEvents.clear();
                Intent aa = new Intent(context, EditTaskAct.class);
                aa.putExtra("judulevnt", getJudulevnt);
                aa.putExtra("ketevnt", getKetevnt);
                aa.putExtra("tglevnt", getTglevnt);
                aa.putExtra("keyevnt", getKeyevnt);
                aa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(aa);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return myEvents.size();
    }

    class MyHolderView extends RecyclerView.ViewHolder{
        TextView judulevnt,ketevnt,wktevnt,tglevnt,keyevnt,active;

        public MyHolderView(View itemView){
            super(itemView);
            judulevnt = itemView.findViewById(R.id.judulevnt);
            ketevnt = itemView.findViewById(R.id.ketevnt);
            wktevnt = itemView.findViewById(R.id.wktevnt);
            tglevnt = itemView.findViewById(R.id.tglevnt);
        }
    }
}

