import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

import java.io.*;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class BTClient {

    private boolean empezo = false;

    public BTClient() throws IOException, NoSuchAlgorithmException {
        // First, instantiate the Client object.
        Client client = new Client(
                // This is the interface the client will listen on (you might need something
                // else than localhost here).
                //InetAddress.getLocalHost(),
                //InetAddress.getByAddress(new byte[]{0, 0, 0, 0}),
                InetAddress.getByName("172.23.66.62"),

                // Load the torrent from the torrent file and use the given
                // output directory. Partials downloads are automatically recovered.
                SharedTorrent.fromFile(
				//Ruta torrent a descargar 
                        new File("/home/isis/clientes/P2P_Redes2018/out/artifacts/Client/torrent/250.torrent"),
				//Creo que ruta donde descarga (Parametro dice 'parentDirectory'
                        new File("/home/isis/clientes/P2P_Redes2018/out/artifacts/Client/torrent")));



// You can optionally set download/upload rate limits
// in kB/second. Setting a limit to 0.0 disables rate
// limits.
        client.setMaxDownloadRate(0.0);
        client.setMaxUploadRate(0.0);

// At this point, can you either call download() to download the torrent and
// stop immediately after...


        client.download();

        client.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {

                Client client = (Client) observable;
                float progress = client.getTorrent().getCompletion();
                // Do something with progress.
                long init = 0;
                if(progress >= 0 && !empezo) {
                    init = System.currentTimeMillis();
                    System.out.println("Init: " + init);
                    empezo = true;
                }
                if(progress >= 100)
                {
                    long fin = System.currentTimeMillis();
                    System.out.println("Fin: " + fin);
                    PrintWriter writer = null;
                    try {
                        long time = fin - init;
                        System.out.println("Time: " + time);
                        writer = new PrintWriter(new Date().toString() + ".txt", "UTF-8");

                        writer.println("Cliente: 172.23.66.62" );

                        writer.println("Archivo: 250.rar");
                        writer.println("Fecha: " + new Date().toString());
                        writer.println("Tiempo: " + time);
                        writer.println("Paquetes: " + client.getTorrent().getPieceCount());
                        writer.println("Paquetes 2: " + client.getTorrent().getAvailablePieces().length());
                        writer.println("Paquetes recibidos: " + client.getTorrent().getCompletedPieces());
                        writer.println("Estado: " + client.getTorrent().getCompletion());
                        writer.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

// Or call client.share(...) with a seed time in seconds:

        client.share(3600);
// Which would seed the torrent for an hour after the download is complete.

// Downloading and seeding is done in background threads.
// To wait for this process to finish, call:
        client.waitForCompletion();


// At any time you can call client.stop() to interrupt the download.
        //client.stop();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        BTClient client = new BTClient();



    }
}
