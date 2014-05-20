package meta;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;


public class MetaExtractor
{
	
	public static int pos(String s)
	{
		 int sp_pos=0;
         for(int i=0;i<s.length();i++)
         {
         	if(s.charAt(i)==' ')
         	{
         		sp_pos=i;
         		break;
         	}
         }
         return sp_pos;
	}
	
    public static void main( String[] args )
    {
        try
        {
            File jpgFile = new File( "F:\\life@IIIT-D\\5 year celebration\\WP_000981.jpg" );
            Metadata metadata = ImageMetadataReader.readMetadata( jpgFile );

            // Read Exif Data
            Directory directory = metadata.getDirectory( ExifIFD0Directory.class );
            Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
            
            if( directory != null && gpsDir!=null)
            {
                String latitude = gpsDir.getDescription(GpsDirectory.TAG_GPS_LATITUDE);
                String latitudeRef = gpsDir.getDescription(GpsDirectory.TAG_GPS_LATITUDE_REF);
                
                String longitude = gpsDir.getDescription(GpsDirectory.TAG_GPS_LONGITUDE);
                String longitudeRef = gpsDir.getDescription(GpsDirectory.TAG_GPS_LONGITUDE_REF);
                
                String alt= gpsDir.getDescription(GpsDirectory.TAG_GPS_ALTITUDE);
                String altRef= gpsDir.getDescription(GpsDirectory.TAG_GPS_ALTITUDE_REF);
              
                System.out.println("Latitude :" + latitude + " || " + latitudeRef);
                System.out.println("Longitude:" + longitude + " || " + longitudeRef);
                System.out.println("Altitude:" + alt + " || " + altRef);
                
                double lat=0f;
                double lang=0f;
                
                latitude=latitude.replace("°", "");
                longitude=longitude.replace("°", "");
                
                /*int sp_pos=0;
                for(int i=0;i<latitude.length();i++)
                {
                	if(latitude.charAt(i)==' ')
                	{
                		sp_pos=i;
                		break;
                	}
                }*/
                
                String latitude_t=latitude.substring(0, pos(latitude)-1).replace(".", "");
                String temp=latitude.substring(pos(latitude)+1, latitude.length());//.replaceAll(".","").replaceAll(" ", "").replaceAll("\"", "").replaceAll("'", "");
                String temp1=temp.substring(0,pos(temp)-3);
                String temp2=temp.substring(pos(temp)+1, temp.length()-1).replaceAll("\\.", "");
                temp1=temp1.concat(temp2);               
                latitude_t=latitude_t.concat(".").concat(temp1);            		
                System.out.println("@@"+latitude_t);
                
                temp="";
                temp1="";
                temp2="";
                String longitute_t=longitude.substring(0, pos(longitude)-1).replace(".", "");
                temp=longitude.substring(pos(longitude)+1, longitude.length());//.replaceAll(".","").replaceAll(" ", "").replaceAll("\"", "").replaceAll("'", "");
                temp1=temp.substring(0,pos(temp)-3);
                temp2=temp.substring(pos(temp)+1, temp.length()-1).replaceAll("\\.", "");
                temp1=temp1.concat(temp2);               
                longitute_t=longitute_t.concat(".").concat(temp1);                		
                System.out.println("@@"+longitute_t);
                
                lat=Double.parseDouble(latitude_t);
                lang=Double.parseDouble(longitute_t);
                
                Collection<Tag> taglist=directory.getTags();
                //System.out.println(taglist);
                String []data=taglist.toString().split(",");
                for(int i=0;i<data.length;i++)
                {
                	System.out.println(data[i]);
                }
                
                System.out.println("Adjust zoom level");
                Integer zoom=Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                System.out.println("1. roadmap");
                System.out.println("2. terrain");
                System.out.println("3. satellite");
                System.out.println("4. hybrid");
                
                System.out.print("Choice : ");
                String maptype="";
                Integer choice =Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
                if(choice==1)
                	maptype="roadmap";
                if(choice==2)
                	maptype="terrain";
                if(choice==3)
                	maptype="satellite";
                if(choice==4)
                	maptype="hybrid";
                
                for(int i=1;i<=zoom;i++)
                {
                	//URL url = new URL("http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lang+"&zoom="+i+"&size=640x640&scale=2&maptype="+maptype+"&sensor=false&format=jpg");
                	
                	//low res
                	URL url = new URL("http://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lang+"&zoom="+i+"&size=640x640&maptype="+maptype+"&sensor=false&format=jpg");
                	InputStream in = new BufferedInputStream(url.openStream());
                	ByteArrayOutputStream out = new ByteArrayOutputStream();
                	byte[] buf = new byte[1024];
                	int n = 0;
                	while (-1!=(n=in.read(buf)))
                	{
                		out.write(buf, 0, n);
                	}
                	out.close();
                	in.close();
                	byte[] response = out.toByteArray();
                
                	FileOutputStream fos = new FileOutputStream("C://test//map//image_zoom"+i+".jpg");
                	fos.write(response);
                	fos.close();
                	System.out.println("Retrive image "+i+" complete !!!");
                }
                System.out.println("Image write complete !!!");
            }
            else
            {
                System.out.println( "EXIF is null" );
            }

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
                
    }
}
