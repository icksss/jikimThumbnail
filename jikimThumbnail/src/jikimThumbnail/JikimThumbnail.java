package jikimThumbnail;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

public class JikimThumbnail {

	 /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
            
    		
    		File f = new File("C:\\test\\test2.jpg");
            BufferedImage src = ImageIO.read(f);
            int wantWeight = 1000;
            int wantHeight = 1000;
            
            
            /*
             *원하는 size 로 width,height 로 줄이기(찌그러짐) 
             * */
            ResampleOp resampleOp = new ResampleOp (100,205);
            resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
            BufferedImage rescaled = resampleOp.filter(src, null);
            
            ImageIO.write(rescaled, "JPG", 
                            new File("C:\\test\\thumbnail_1.jpg"));
            /*
             *회전 90
             * */
            try{
            	ImageIO.write(rotate90(rescaled), "JPG", 
                        new File("C:\\test\\thumbnail_1_rotate90.jpg"));
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            /*
             *회전 180
             * */
            try{
            	ImageIO.write(rotate180(rescaled), "JPG", 
                        new File("C:\\test\\thumbnail_1_rotate180.jpg"));
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            /*
             *회전 270
             * */
            try{
            	ImageIO.write(rotate270(rescaled), "JPG", 
                        new File("C:\\test\\thumbnail_1_rotate270.jpg"));
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            /*
             *가로든 세로든 줄어드는 비율이 높은 쪽을 기준으로 으로 만들기(적게 줄이는 쪽기준) 
             *만드려는 size 보다 작은 이미지는 확대 된다.
             * */
            //실제 이미지 size
            int imageWidth = src.getWidth(null);  
            int imageHeight = src.getHeight(null); 
            
            //double ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
            double ratio =Math.max((double)wantWeight/ (double)imageWidth, (double)wantHeight/ (double)imageHeight);
            //System.out.println("(double)wantWeight/ (double)imageWidth : "+(double)wantWeight/ (double)imageWidth);
            //System.out.println(" (double)wantHeight/ (double)imageHeight : "+ (double)wantHeight/ (double)imageHeight);
            //비율대로 만들어지는 실제 이미지 size 구하기
            int w = (int)(imageWidth * ratio);
        	int h = (int)(imageHeight * ratio);
        	
        	try{
        		makeThumbnail(src,w,h,"thumbnail2.jpg");
        		 /*
                 *회전 90 width,height  값 주의.
                 * */
        		makeThumbnail(rotate90(src),h,w,"thumbnail_2_rotate90.jpg");
        	}catch(Exception e){
        		e.printStackTrace();
        	}
           /*
             *썸네일이 원본 보다 크지 않도록 만들기 
             * */
            try{
            	ratio =Math.min( (double)wantWeight/ (double)wantHeight, 1);
            	w = (int)(imageWidth * ratio);
            	h = (int)(imageHeight * ratio);
        		makeThumbnail(src,w,h,"thumbnail3.jpg");
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            
            /*
             *가로의(width) 기준으로 확대/축소
             * */
            try{
            	ratio =(double)wantWeight/ (double)imageWidth;
            	w = (int)(imageWidth * ratio);
            	h = (int)(imageHeight * ratio);
        		makeThumbnail(src,w,h,"thumbnail4.jpg");
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            
            /*
             *세로의(height) 기준으로 확대/축소
             * */
            try{
            	ratio =(double)wantHeight/ (double)imageHeight;
            	w = (int)(imageWidth * ratio);
            	h = (int)(imageHeight * ratio);
        		makeThumbnail(src,w,h,"thumbnail5.jpg");
        	}catch(Exception e){
        		e.printStackTrace();
        	}
            
            

            
    }
    
    public static void makeThumbnail(BufferedImage src , int w, int h,String fileName) throws IOException {
    	BufferedImage thumbImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
		Graphics2D graphics = thumbImage.createGraphics();  
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        graphics.drawImage(src, 0,0, w, h, null);  
    	
        ResampleOp resampleOp2 = new ResampleOp (w, h);
        resampleOp2.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
        //resampleOp2.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
        BufferedImage rescaled2 = resampleOp2.filter(thumbImage, null);
        ImageIO.write(rescaled2, "JPG", 
                        new File("C:\\test\\"+fileName));
    	
    }
	public  static BufferedImage rotate90(BufferedImage bi)
    {
        int width = bi.getWidth();
        int height = bi.getHeight();
        
        BufferedImage biFlip = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++)
                biFlip.setRGB(height-1-j, i, bi.getRGB(i, j));
        
        return biFlip;
    }

    public static  BufferedImage rotate270(BufferedImage bi)
    {
        int width = bi.getWidth();
        int height = bi.getHeight();
        
        BufferedImage biFlip = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                biFlip.setRGB(j, width-1-i, bi.getRGB(i, j));
            }
        }
        return biFlip;
    }
    
    public static  BufferedImage rotate180(BufferedImage bi)
    {
        int width = bi.getWidth();
        int height = bi.getHeight();
        
        BufferedImage biFlip = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                biFlip.setRGB(width-1-i, height-1-j, bi.getRGB(i, j));
            }
        }
        return biFlip;
    }	
}
