package oweissbarth.de.edithosts;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HostsAdapter extends RecyclerView.Adapter<HostsAdapter.ViewHolder>{
    private HostsFile hosts;

    public HostsAdapter(HostsFile hosts){
        this.hosts = hosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hosts_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView host = holder.view.findViewById(R.id.textViewHost);
        TextView ip = holder.view.findViewById(R.id.textViewIP);
        host.setText(this.hosts.get(position).getHostname());
        ip.setText(this.hosts.get(position).getIp());
    }

    @Override
    public int getItemCount() {
        return this.hosts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            Log.d("HostsAdapter", "ContextMenuInfo: "+ menuInfo);
            contextMenu.setHeaderTitle("Host");
            MenuItem edit = contextMenu.add(Menu.NONE, 0, 0, "Edit");
            MenuItem remove =  contextMenu.add(Menu.NONE, 1, 1, "Remove");
            edit.setOnMenuItemClickListener(onEdit);
            remove.setOnMenuItemClickListener(onRemove);
        }

        private MenuItem.OnMenuItemClickListener onEdit = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(view.getContext(), EditHostActivity.class);
                intent.putExtra("create", false);
                intent.putExtra("index", getAdapterPosition());
                view.getContext().startActivity(intent);
                return true;
            }
        };

        private MenuItem.OnMenuItemClickListener onRemove = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                hosts.remove(getAdapterPosition());
                notifyDataSetChanged();
                return true;
            }
        };

    }


}
