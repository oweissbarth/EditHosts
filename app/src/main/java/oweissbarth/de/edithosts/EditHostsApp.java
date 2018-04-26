package oweissbarth.de.edithosts;

import android.app.Application;

public class EditHostsApp extends Application {
    private HostsFile hosts;

    public HostsFile getHosts(){
        return this.hosts;
    }

    public void setHosts(HostsFile hosts){
        this.hosts = hosts;
    }
}
