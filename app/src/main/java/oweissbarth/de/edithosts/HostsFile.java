package oweissbarth.de.edithosts;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class HostsFile extends ArrayList<Host> {

    public boolean write(){
        try{
            createBackup();

        }catch (IOException e){
            Log.e("HostsFile", "failed to create backup! aborting..."+e.getMessage());
            return false;
        }
        return writeFileWithSU("/system/etc/hosts",  this.getText());
    }

    public void read(){
        File file = new File("/system/etc/hosts");
        StringBuilder sb = new StringBuilder();
        this.clear();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!= null){
                line = line.trim();
                if(line.length() == 0 || line.startsWith("#")){
                    continue;
                }
                Log.d("HostsFile", "line length: "+line.length());
                String ip = line.split("\\s+")[0];
                String host = line.split("\\s+")[1];
                this.add(new Host(ip, host));
            }
            reader.close();
        }catch(IOException e){
            Log.e("HostsFile", "Could not read file:"+ e.getMessage());
        }
    }

    public void createBackup() throws IOException{
        File original = new File("/system/etc/hosts");

        BufferedReader reader = new BufferedReader(new FileReader(original));
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[1024];
        String line;
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        reader.close();

        writeFileWithSU("/system/etc/hosts.bak", sb.toString());
    }

    public void restoreBackup(){

    }

    @NonNull
    private String getText(){
        StringBuilder sb = new StringBuilder();
        for(Host h : this){
            sb.append(h.getIp()+"\t"+h.getHostname()+"\n");
        }
        return sb.toString();
    }

    private boolean writeFileWithSU(String filename, String content) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("mount -o rw,remount /system\n");
            os.flush();
            os.writeBytes("echo '" + content + "' > " + filename+"\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch(InterruptedException e){
            e.printStackTrace();
            return false;
        }
    }
}
