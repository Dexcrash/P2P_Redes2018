import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

public class BTServer {

    public BTServer() throws IOException, NoSuchAlgorithmException {

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

	//Codigo para crear torrent de un archivo
        String sharedFile = "/home/s6g4/servidores/P2P_Redes2018/out/artifacts/Server/torrent/250.rar";

        try {
            Tracker tracker = new Tracker( InetAddress.getLocalHost() );
            tracker.start();
            System.out.println("Tracker running.");

            System.out.println( "create new .torrent metainfo file..." );
            Torrent torrent = Torrent.create(new File(sharedFile), tracker.getAnnounceUrl().toURI(), "createdByGrupo4");

            System.out.println("save .torrent to file...");

            FileOutputStream fos = new FileOutputStream("/home/s6g4/servidores/P2P_Redes2018/out/artifacts/Server/torrent");
            torrent.save( fos );
            fos.close();

            tracker.stop();

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        // First, instantiate a Tracker object with the port you want it to listen on.
// The default tracker port recommended by the BitTorrent protocol is 6969.
        Tracker tracker = new Tracker(new InetSocketAddress(6969));

// Then, for each torrent you wish to announce on this tracker, simply created
// a TrackedTorrent object and pass it to the tracker.announce() method:
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".torrent");
            }
        };



        System.out.println(System.getProperty("user.dir"));
        //Directorio con torrents que quiere anunciar
        for (File f : new File("./torrent").listFiles(filter)) {

            tracker.announce(TrackedTorrent.load(f));
        }

        System.out.println(tracker.getAnnounceUrl().toString());
        System.out.println(tracker.getTrackedTorrents().toString());

// Once done, you just have to start the tracker's main operation loop:
        tracker.start();

// You can stop the tracker when you're done with:
        //tracker.stop();

    }
}
