package hu.ait.travelmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hu.ait.travelmap.data.EntryData;


public class EntryAdaptor extends RecyclerView.Adapter<EntryAdaptor.ViewHolder> {

    private List<EntryData> listEntry;
    private List<String> listKeys;
    private Context context;
    public static final String ENTRY_NAME = "Entry Name";

    public EntryAdaptor(Context context){

        this.context = context;

        listEntry = new ArrayList<EntryData>();
        listKeys = new ArrayList<String>();
    }

    @Override
    public EntryAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View entryRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.entry_row, parent, false);
        return new ViewHolder(entryRow);
    }

    @Override
    public void onBindViewHolder(final EntryAdaptor.ViewHolder holder, final int position) {

        holder.tvTitle.setText(listEntry.get(position).getTitle());
        holder.tvDate.setText(listEntry.get(position).getDate());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry(holder.getAdapterPosition());
            }
        });
        holder.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenMap = new Intent();
                intentOpenMap.setClass(context, FullMapsActivity.class);
                context.startActivity(intentOpenMap);
            }
        });

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryModel.getInstance().setEntryData(listEntry.get(holder.getAdapterPosition()));

                Intent intentStartEntry = new Intent();
                intentStartEntry.setClass(context, EntryViewActivity.class);

                intentStartEntry.putExtra(ENTRY_NAME, holder.tvTitle.getText().toString());

                context.startActivity(intentStartEntry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEntry.size();
    }


    public void addEntry(EntryData entry, String key) {
        listEntry.add(entry);
        listKeys.add(key);
        notifyDataSetChanged();
    }

    public void deleteEntry(int position) {
        FirebaseDatabase.getInstance().getReference("entry").child(listKeys.get(position)).removeValue();
        listEntry.remove(position);
        listKeys.remove(position);

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private Button btnMap;
        private Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            btnMap = (Button) itemView.findViewById(R.id.btnMap);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
