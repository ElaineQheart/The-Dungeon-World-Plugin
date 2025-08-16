package me.elaineqheart.theDungeon.data;

import me.elaineqheart.theDungeon.TheDungeon;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UFG {


    public static String path;
    public final static String dungeonLobby = "The-Dungeon";
    public final static String dungeon = "world_minecraft_the_dungeon";

    //credits:
    //https://gist.github.com/ggangesh/a1a8df059c4de787708882ecd2964aff

    public void copyInputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    public void download(String stringUrl, File file, File output) {

        new Thread(new Runnable() {
            public void run() {

                try {
                    long startTime = System.currentTimeMillis();
                    TheDungeon.getInstance().getLogger().info("Downloading dungeon world...");

                    URL url = new URL(stringUrl);
                    url.openConnection();
                    InputStream reader = url.openStream();

                    FileOutputStream writer = new FileOutputStream(file);
                    byte[] buffer = new byte[102400];
                    int totalBytesRead = 0;
                    int bytesRead = 0;

                    //Reading ZIP file 20KB blocks at a time

                    while ((bytesRead = reader.read(buffer)) > 0) {
                        writer.write(buffer, 0, bytesRead);
                        buffer = new byte[102400];
                        totalBytesRead += bytesRead;
                    }

                    long elapsedTime = System.currentTimeMillis() - startTime;
                    TheDungeon.getInstance().getLogger().info("Done. " + totalBytesRead + " bytes read (" + elapsedTime + " millseconds).\n");
                    writer.close();
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                unZipIt(file,output);
            }
        }).start();
    }

    public void unZipIt(File file, File output) {

        try {
            long startTime = System.currentTimeMillis();
            TheDungeon.getInstance().getLogger().info("Unzipping the file...");
            ZipFile zipFile = new ZipFile(file);

            Enumeration zipEntries = zipFile.entries();

            String OUTDIR = output + File.separator;
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.isDirectory()) {
                    //System.out.println("      Extracting directory: " + OUTDIR + zipEntry.getName());

                    new File(OUTDIR + zipEntry.getName()).mkdir();
                    continue;
                }

                //System.out.println("       Extracting file: " + OUTDIR + zipEntry.getName());

                copyInputStream(zipFile.getInputStream(zipEntry), new BufferedOutputStream(new FileOutputStream(OUTDIR + zipEntry.getName())));
            }

            zipFile.close();

            file.delete();

            Stream<Path> stream = Files.list(TheDungeon.getInstance().getDataFolder().toPath()).filter(Files::isDirectory);
            path = stream.toList().getFirst().toFile().getPath().replace('\\', '/') + "/";

            copyDimension();
            copyDatapacks();

            long elapsedTime = System.currentTimeMillis() - startTime;
            TheDungeon.getInstance().getLogger().info("Done. (" + elapsedTime + " millseconds).\n");
            TheDungeon.getInstance().getLogger().info("Please restart the server for the dungeon dimension to work.");

        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            throw new RuntimeException(ioe);
        }
    }

    private void copyDatapacks() {
        File sourceWorldFolder = new File(Bukkit.getWorldContainer(), path + dungeonLobby);
        File targetWorldFolder = new File(Bukkit.getWorldContainer(), "world");

        try {
            // Copy the dungeon template world folder
            copyWorldFolder(sourceWorldFolder, targetWorldFolder, "datapacks");
            copyWorldFolder(sourceWorldFolder, targetWorldFolder, "generated");
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to copy datapacks. " + e);
        }
    }

    private void copyDimension() {
        File sourceWorldFolder = new File(Bukkit.getWorldContainer(), path + dungeon);
        File targetWorldFolder = new File(Bukkit.getWorldContainer(), dungeon);

        try {
            // Copy the dungeon template world folder
            copyWorldFolder(sourceWorldFolder, targetWorldFolder, null);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to create dungeon world. " + e);
        }
        return;
    }

    private void copyWorldFolder(File source, File target, String partialDir) throws IOException {
        if (!target.exists()) {
            target.mkdirs();
        }

        if(partialDir != null) {
            File srcFile = new File(source, partialDir);
            File destFile = new File(target, partialDir);
            if (srcFile.isDirectory()) {
                copyWorldFolder(srcFile, destFile, null);
            } else {
                if(destFile.exists()) Files.delete(destFile.toPath());
                Files.copy(srcFile.toPath(), destFile.toPath());
            }
            return;
        }

        for (String file : source.list()) {
            File srcFile = new File(source, file);
            File destFile = new File(target, file);
            if (srcFile.isDirectory()) {
                copyWorldFolder(srcFile, destFile, null);
            } else {
                if(destFile.exists()) Files.delete(destFile.toPath());
                Files.copy(srcFile.toPath(), destFile.toPath());
            }
        }
    }

}
