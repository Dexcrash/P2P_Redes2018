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
                InetAddress.getLocalHost(),

                // Load the torrent from the torrent file and use the given
                // output directory. Partials downloads are automatically recovered.
                SharedTorrent.fromFile(
				//Ruta torrent a descargar 
                        new File("C:\\Users\\juanp\\Desktop\\pruebatr\\250.torrent"),
				//Creo que ruta donde descarga (Parametro dice 'parentDirectory'
                        new File("C:\\Users\\juanp\\Desktop\\pruebatr")));

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
        client.setMaxDownloadRate(500.0);
        client.setMaxUploadRate(500.0);

// At this point, can you either call download() to download the torrent and
// stop immediately after...
        client.download();

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
