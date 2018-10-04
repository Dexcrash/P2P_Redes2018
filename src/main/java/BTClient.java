import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

public class BTClient {

    public BTClient() throws IOException, NoSuchAlgorithmException {
        // First, instantiate the Client object.
        Client client = new Client(
                // This is the interface the client will listen on (you might need something
                // else than localhost here).
                //InetAddress.getLocalHost(),
                //InetAddress.getByAddress(new byte[]{0, 0, 0, 0}),
                InetAddress.getByName("172.23.66.61"),

                // Load the torrent from the torrent file and use the given
                // output directory. Partials downloads are automatically recovered.
                SharedTorrent.fromFile(
				//Ruta torrent a descargar 
                        new File("/home/isis/clientes/P2P_Redes2018/out/artifacts/Client/torrent/250.torrent"),
				//Creo que ruta donde descarga (Parametro dice 'parentDirectory'
                        new File("/home/isis/clientes/P2P_Redes2018/out/artifacts/Client/torrent")));

        client.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Client client = (Client) observable;
                float progress = client.getTorrent().getCompletion();
                // Do something with progress.
            }
        });

// You can optionally set download/upload rate limits
// in kB/second. Setting a limit to 0.0 disables rate
// limits.
        client.setMaxDownloadRate(0);
        client.setMaxUploadRate(0);

// At this point, can you either call download() to download the torrent and
// stop immediately after...

        long init = System.currentTimeMillis();
        System.out.println("Inicia a descargar:");

        client.download();

// Or call client.share(...) with a seed time in seconds:
        long fin = System.currentTimeMillis();
        System.out.println("Se termina de descargar en: " + (fin - init) + "\nComenzando a compartir");

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
