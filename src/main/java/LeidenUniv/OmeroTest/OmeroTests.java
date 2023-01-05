/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package LeidenUniv.OmeroTest;

import net.imglib2.type.numeric.RealType;
import omero.gateway.model.ImageData;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;
import LeidenUniv.Omero.Open_Omero_Dataset; //needed for the first part. do not remove
import LeidenUniv.Omero.getOmeroDatasetAndAttachData;
import ij.IJ;
import ij.measure.ResultsTable;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 */
@SuppressWarnings("unused")
@Plugin(type = Command.class, menuPath = "Plugins>Test>Imagej2OmeroTest")
public class OmeroTests<T extends RealType<T>> implements Command {
    @Override
    public void run() {
    	// The part below is used for opening all images in a dataset, this only works directly from FIJI so you need to compile and add the plugin to the plugins folder
    	/* 
    	Open_Omero_Dataset ood = new Open_Omero_Dataset();
    	ood.run("");
    	*/
    	
    	// The part below shows how to attach data to an image, if you do not open the image it can be tested from eclipse.
    	// Otherwise it needs to be compiled and added to the fiji plugin folder
    	getOmeroDatasetAndAttachData godaad = new getOmeroDatasetAndAttachData();
    	Collection<ImageData> images = godaad.getImageCollection();// we now have a collection of images we can run through
    	if (images!=null){// if there are images
    		int counter=0;
    		Iterator<ImageData> image = images.iterator(); // create an iterator to go through images 
    		while (image.hasNext()) {// run through all the images
    			counter++;
    			ImageData data = image.next(); // gets the current image
    			IJ.log("Loading image "+counter+" of "+images.size()); // provides progress feedback for users

    			try {
                	//ImagePlus timp = godaad.openImagePlus(data.getId(), data.getGroupId()); // loads the image if needed
	                ResultsTable rt2 = new ResultsTable(); // create a result table with some data
	                rt2.incrementCounter();
	                rt2.addValue("Test string", "test 1");
	                rt2.addValue("Test", counter);
	                godaad.attachDataToImage(rt2, 1, data, "table name"); // this is to add a resultstable
	                godaad.attachDataToDataset(rt2, 1, "Named table"); // this is to add a resultstable to a dataset
	                // this part will create a file to attach
	                File file=null; 
	                try {
	                	file = new File("temp.txt");
	                    if (file.createNewFile()) {
	                      System.out.println("File created: " + file.getName());
	                    } else {
	                      System.out.println("File already exists.");
	                    }
	                  } catch (IOException e) {
	                    System.out.println("An error occurred.");
	                    e.printStackTrace();
	                  }
	                // till here
	                godaad.attachFile(file, "attached file", "Textfile", "txt", data); // this will attach the file to the image
               
                } catch (Exception e) {
                	 IJ.log("Error Loading image "+counter+" of "+images.size());
                	 IJ.log(e.getMessage());
                    	StackTraceElement[] t = e.getStackTrace();
                    	for (int i=0;i<t.length;i++){
                    		IJ.log(t[i].toString());
                    	}
                }
    			IJ.log("Finished image "+counter+" of "+images.size());
    		}
    	}
    }

    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
    	@SuppressWarnings("rawtypes")
		OmeroTests gf = new OmeroTests();
    	gf.run();
    }

}
