package oweissbarth.de.edithosts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity {
    private HostsAdapter adapter;
    private HostsFile hosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(((EditHostsApp)this.getApplication()).getHosts() == null){
            this.hosts = new HostsFile();
            ((EditHostsApp)this.getApplication()).setHosts(hosts);
            hosts.read();
        }else{
            this.hosts = ((EditHostsApp)this.getApplication()).getHosts();
        }



        Log.d("MainActivity", hosts.toString());

        RecyclerView recyclerView = findViewById(R.id.hosts);

        LinearLayoutManager layoutMan = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutMan);

        this.adapter = new HostsAdapter(hosts);

        recyclerView.setAdapter(this.adapter);

        registerForContextMenu(recyclerView);

    }

    public void addEntry(View view){
        Intent intent = new Intent(this, EditHostActivity.class);
        intent.putExtra("create", true);
        startActivityForResult(intent, 0);
    }

    public void onResume() {
        super.onResume();
        Log.d("MainActivity", "HostFile length: "+this.hosts.size());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hosts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload_file:
                askForConfirmation("Reload hosts file",
                        "This will discard all local changes.",
                        new Callback() {
                            @Override
                            public void execute() {
                                hosts.read();
                                adapter.notifyDataSetChanged();
                            }
                        });
                return true;
            case R.id.action_write_file:
                askForConfirmation("Write hosts file",
                        "This will overwrite all contents of /etc/hosts. Only continue if you know what you are doing!",
                        new Callback() {
                            @Override
                            public void execute() {
                                boolean result = hosts.write();
                                Log.i("MainActivity", "writing result: "+result);
                                adapter.notifyDataSetChanged();
                            }
                        });
                return true;
            default:
                return false;
        }
    }

    private void askForConfirmation(String title, String message, final Callback callback){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        callback.execute();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    interface Callback{
        void execute();
    }


}
