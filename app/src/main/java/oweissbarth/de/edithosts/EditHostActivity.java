package oweissbarth.de.edithosts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EditHostActivity extends AppCompatActivity {
    private boolean create;
    private int originalIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_host);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.create = intent.getBooleanExtra("create", false);
        this.originalIndex = intent.getIntExtra("index", -1);

        if(!create){
            Host h = ((EditHostsApp)this.getApplication()).getHosts().get(originalIndex);
            ((EditText)findViewById(R.id.hostname_edit)).setText(h.getHostname());
            ((EditText)findViewById(R.id.ip_edit)).setText(h.getIp());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_apply:
                EditText hostField = (EditText) findViewById(R.id.hostname_edit);
                EditText ipField = (EditText) findViewById(R.id.ip_edit);
                String hostname = hostField.getText().toString();
                String ip = ipField.getText().toString();
                HostsFile hosts = ((EditHostsApp)this.getApplication()).getHosts();
                Log.d("EditHostActivity", "call by reference?:"+(hosts == ((EditHostsApp)this.getApplication()).getHosts()));
                Log.d("EditHostActivity", hosts.toString());

                if(this.create){
                    hosts.add(new Host(ip, hostname));
                    Log.d("EditHostActivity", "HostFile length: "+ hosts.size());
                }else{
                    hosts.set(this.originalIndex, new Host(ip, hostname));
                }

                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_hosts_menu, menu);
        return true;
    }
}
