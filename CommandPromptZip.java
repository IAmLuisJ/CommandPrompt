 import java.io.*;
 import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;
 import java.util.zip.ZipEntry;
 import java.util.zip.ZipInputStream;
 import java.util.zip.ZipOutputStream;

 import javax.swing.filechooser.FileSystemView;

 //Luis Juarez
 // Purpose: recreating command prompt in Java

public class CommandPromptZip {
    static final int BUFFER = 2048;
	public static void main(String[] args) {
		String currentPath = System.getProperty("user.dir");
		String response = "";
		System.out.println("Luis' Operating System [ver1.1]\n(c) My Corporation. All rights reserved.\n\n");
		response = printInputScreen(currentPath).toLowerCase();
		while (! response.equals("exit")){
			if (response.toLowerCase().startsWith("dir")) 
				dir(currentPath);
			else if (response.startsWith("unzip"))
				unzip(response, currentPath);
			else if (response.startsWith("zip"))
				zip(response, currentPath);
			else if (response.startsWith("zipAdd"))
				zipAdd(response, currentPath);
			else if (response.startsWith("help")) 
				System.out.println("Help if not available.");
			else if(response.toLowerCase().startsWith("cd")) 
				System.out.println("cd if not available.");
			else if (response.toLowerCase().startsWith("md")) 
				System.out.println("md if not available.");
			else if (response.toLowerCase().startsWith("rd"))
				System.out.println("rd if not available.");
			else if (response.toLowerCase().startsWith("del"))
				System.out.println("del if not available.");
			else if (response.toLowerCase().startsWith("copy"))
				System.out.println("copy if not available.");
			else if (response.toLowerCase().startsWith("rename"))
				System.out.println("rename if not available.");			
			else System.out.println("Error: What kind of a command is " + response + "???");
			response = printInputScreen(currentPath).toLowerCase();
			}
		System.out.println("Exiting Tom's Operating System [ver1.1]");
	}
	public static void zip(String response, String currentPath){
		//syntax: zip fileName.ext zipName.zip
		String fileName = response.substring(3, response.indexOf(".")).trim();
		String fileExtension = response.substring(response.indexOf("."),response.indexOf(" ", response.indexOf("."))).trim();
		String zipName = response.substring(response.indexOf(" ", response.indexOf(".")), response.lastIndexOf(".")).trim();
		String zipExtension = response.substring(response.lastIndexOf("."), response.length()).trim();


        BufferedInputStream origin = null;
        FileOutputStream dest;
        try {
            dest = new FileOutputStream(currentPath +"//" + zipName + zipExtension);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            File zipFile = new File(currentPath + "//" + fileName + fileExtension);
            if (zipFile.exists())
            {
                FileInputStream fi = new FileInputStream(fileName + fileExtension);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(fileName + fileExtension);
                out.putNextEntry(entry);
                int count;
                while((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
                out.close();
                dest.close();
                System.out.println("Adding " + fileName + fileExtension + " to zip file");
            } else {
                System.out.println(fileName + " does not exist!");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }


	}
	public static void zipAdd(String response, String currentPath){
		//syntax: zipAdd fileName.ext zipName.zip
		String fileName = response.substring(6, response.indexOf(".")).trim();
		String fileExtension = response.substring(response.indexOf("."),response.indexOf(" ", response.indexOf("."))).trim();
		String zipName = response.substring(response.indexOf(" ", response.indexOf(".")), response.lastIndexOf(".")).trim();
		String zipExtension = response.substring(response.lastIndexOf("."), response.length()).trim();

        unzip(zipName + zipExtension, currentPath); // unzip - need to unzip to temp location
        // add file
        //zip file + old files


	}
	public static void unzip(String response, String currentPath){
		//syntax:  unzip fileName.zip
		String fileName = response.substring(5, response.indexOf(".")).trim();
        String fileExtension = response.substring(response.indexOf("."));

        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(fileName + fileExtension);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " +entry);
                int count;
                byte data[] = new byte[BUFFER];
                // write the files to the disk
                FileOutputStream fos = new FileOutputStream(entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

	}
	public static String printInputScreen(String currentPath){
		Scanner input = new Scanner(System.in);
		System.out.print(currentPath + ">");
		return input.nextLine();		
	}
	public static void dir(String currentPath){
		int totalSize = 0;
		int fileCount = 0;
		int dirCount = 0;
		File dir = new File(currentPath);
		//next 5 lines needed to print drive letter and name of current path
		File rootDirectory = new File(Paths.get(dir.getPath()).getRoot().toString());
		String driveName = FileSystemView.getFileSystemView().getSystemDisplayName (rootDirectory);
		System.out.printf(" Volume in drive %s is %s\n", rootDirectory, driveName);
		System.out.printf("\n  Directory of %s \n\n", currentPath);
		File[] filesList = dir.listFiles();
		for(File f : filesList){
			String fileType = "";
			totalSize += f.length();
			if(f.isDirectory())
                fileType = "<DIR>     ";
				dirCount ++;
			if(f.isFile()){
				fileType = "<FILE>    ";
				fileCount ++;
            }
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
			System.out.printf("%s   %s  %5d  %s\n", sdf.format(f.lastModified()), fileType, f.length(), f.getName());
	    }
		System.out.printf("%18d File(s)  %,16d bytes\n",fileCount, totalSize);
		System.out.printf("%18d Dir(s)   %,16d bytes free\n\n",dirCount, dir.getFreeSpace());
	}
	public static File[] filesThatMatchThis(String strFileName, String strPath){   //note: strFileName can be either file name or path + file name
	    File copyPathFile = new File(strPath);
	    StringBuilder sb = new StringBuilder(strFileName.length() + 25);
	    //convert fileName to regex
	    sb.append('^');
	    for (int i = 0; i < strFileName.length(); ++i) {
	      char c = strFileName.charAt(i);
	      if (c == '*')   sb.append(".*"); 
	      else if (c == '?')  sb.append('.');
	      else if ("\\.[]{}()+-^$|".indexOf(c) >= 0) {
	        sb.append('\\');
	        sb.append(c);
	      }
	      else    sb.append(c);
	    }
	    sb.append('$');
	    final Pattern pattern = Pattern.compile(sb.toString()); // Caution: could also throw an exception!
	 //get array of files that meet regex criteria
	    File[] FileList = (copyPathFile).listFiles(new FileFilter(){
	      public boolean accept(File file) {
	        return pattern.matcher(file.getName()).matches();
	      }
	    });
	  return FileList;
	}
}
