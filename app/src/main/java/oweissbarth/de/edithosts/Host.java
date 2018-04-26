package oweissbarth.de.edithosts;

public class Host {
    private String ip;
    private String hostname;

    public Host(String ip, String hostname){
        this.ip = ip;
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return ip + "\t "+ hostname;
    }
}
