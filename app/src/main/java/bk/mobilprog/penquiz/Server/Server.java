package bk.mobilprog.penquiz.Server;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//localhost IP: változó
public class Server {

    private String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Server(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    //fájl letöltése függvénnyel, paraméter: elérési út, + context
    public void DownloadFile(String path, Context context){
        //htdocs -> database -> penquizdb.db
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream outputStream = context.openFileOutput("penquizdb.db", Context.MODE_PRIVATE);
            byte[] data = new byte[1024];
            int read;
            while((read = in.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }
            in.close();
            outputStream.close();
            Log.d("Töltés állapota: ","letöltve");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Töltés állapota: ","Sikertelen letöltés");
            Log.d("Hiba oka: ",e.getMessage());
        }
    }

    //eszköz külső IP címe
    public String externalIP() {

        String externalIPaddress = null;
        try {
            URL url = new URL("http://checkip.amazonaws.com/");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            externalIPaddress = bufferedReader.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return externalIPaddress;
    }

    public boolean isValidIPAddress(String ipAddress, String lowerBound, String upperBound) {
        String[] ipParts = ipAddress.split("\\.");
        String[] lowerBoundParts = lowerBound.split("\\.");
        String[] upperBoundParts = upperBound.split("\\.");

        for (int i = 0; i < 4; i++) {
            int ipPart = Integer.parseInt(ipParts[i]);
            int lowerBoundPart = Integer.parseInt(lowerBoundParts[i]);
            int upperBoundPart = Integer.parseInt(upperBoundParts[i]);
            //Log.d("log:",Integer.toString(ipPart)+Integer.toString(lowerBoundPart)+Integer.toString(upperBoundPart));

            if (ipPart < lowerBoundPart || ipPart > upperBoundPart) {
                return false;
            }
        }

        return true;
    }

}
