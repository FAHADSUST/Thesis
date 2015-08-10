/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news_analysis.isimage;

import item.BorderItem;
import java.util.ArrayList;
import java.util.HashMap;
import news_analysis.ImageBorderDetectionBFS;
import news_analysis.NewsAnalysis;
import static news_analysis.NewsAnalysis.imshow;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 *
 * @author fahad_000
 */
public class IsImage {
    
    
    BorderDetection borderDetection;
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    public IsImage(){ }
    
    public boolean isImage(Mat image){
        Size imageSize = image.size();
        int width = image.width();
        int height = image.height();
        
        borderDetection = new BorderDetection();
        ArrayList<BorderItem> borderItems = borderDetection.getBorder(image, width, height);
        Mat[] subMat = new Mat[borderItems.size()];
        for(int i=0;i<borderItems.size();i++){
            BorderItem item = borderItems.get(i);
            if(item.getHeight()>100 && item.getWidth()>100){
                item = canMaxiMizeBorder(item, item.getMinX(), item.getMaxX(), item.getMinY(), item.getMaxY(), height, width);
                subMat[i]= image.submat(item.getMinX(), item.getMaxX(), item.getMinY(), item.getMaxY());

                //NewsAnalysis.imshow("Sub sub sub" + i, subMat[i]);
                int horizontal[] = horizontalChecked(subMat[i], item.getHeight()-1, item.getWidth()-1);
                int verticle[] = VerticleChecked(subMat[i], item.getHeight()-1, item.getWidth()-1);
                if(horizontal[0] + horizontal[1]> 110 && verticle[0] + verticle[1]>110){
                    
                    return true;
                }
                return true;
            }
        
        }
        
        
        return false;      
    }

    private int[] horizontalChecked(Mat image,int width, int height) {
        int CHEACKED_LINE = (width/2) % 6; 
        int countHisto=0;   
        int maxTAG=-1;
        int start_H_HV=0;
        int End_H_HV=0;
        
        for(int i=0; i< height;i++){  
            if(i > CHEACKED_LINE && i < height - CHEACKED_LINE) {maxTAG=-1; continue;}
            countHisto=0;
            for(int j=0; j< width ; j++){
                if(image.get(i, j)[0] == 255){                    
                    countHisto++;
                   // System.out.print("*");
                }
            }
            if(countHisto > maxTAG) maxTAG = countHisto;
            if(i==CHEACKED_LINE-1) start_H_HV = maxTAG;
            else if(i>CHEACKED_LINE) End_H_HV = maxTAG;
            //System.out.println("");
        }
        
        int res[] = new int[2];
        res[0] = compareWithWidth(width, start_H_HV);
        res[1] = compareWithWidth(width, End_H_HV);
        
        //System.out.println("start_H_HV : "+start_H_HV+" End_H_HV : "+End_H_HV);
        
        return res;
        
    }
    private int[] VerticleChecked(Mat image,int width, int height) {
        
        int CHEACKED_LINE = (height/2) % 6; 
        int countHisto=0;
        int maxTAG=-1;
        int start_H_HV=0;
        int End_H_HV=0;
        
        for(int i=0; i< width;i++){  
            if(i > CHEACKED_LINE && i < width - CHEACKED_LINE) {maxTAG=-1; continue;}
            countHisto=0;
            for(int j=0; j<height  ; j++){
                if(image.get(j, i)[0] == 255){                    
                    countHisto++;
                    //System.out.print("*");
                }
            }
            if(countHisto > maxTAG) maxTAG = countHisto;
            if(i==CHEACKED_LINE-1) start_H_HV = maxTAG;
            else if(i>CHEACKED_LINE) End_H_HV = maxTAG;
           // System.out.println("");
        }
        int res[] = new int[2];
        res[0] = compareWithWidth(height, start_H_HV);
        res[1] = compareWithWidth(height, End_H_HV);
        
        //System.out.println("start_H_HV : "+start_H_HV+" End_H_HV : "+End_H_HV);
        
        return res;
        
    }
    
    public int compareWithWidth(int base, int haveToFind){
        return (int)((haveToFind * 100.0f) / base);
    }
    
    public BorderItem  canMaxiMizeBorder( BorderItem item, int minX, int maxX, int minY, int maxY, int width, int height) {
        int minXTemp; int maxXTemp; int minYTemp; int maxYTemp;
        
        if(minX >1 && minX < maxX-1) minXTemp = minX-2;
        else if(minX >0 && minX < maxX) minXTemp = minX-1;
        else minXTemp = minX;
        
        if(maxX < width -2 && maxX > minX-1 ) maxXTemp = maxX+2;
        else if(maxX < width-1  && maxX > minX ) maxXTemp = maxX+1;
        else maxXTemp = maxX;
        
        if(minY >1 && minY < maxY-1) minYTemp = minY-2;
        else if(minY >0 && minY < maxY) minYTemp = minY-1;
        else minYTemp = minY;
        
        if(maxY < height -2 && maxY > minY-1 ) maxYTemp = maxY+2;
        else if(maxY < height-1  && maxY > minY ) maxYTemp = maxY+1;
        else maxYTemp = maxY;
        
        item.setMinX(minXTemp);
        item.setMaxX(maxXTemp);
        item.setMinY(minYTemp);
        item.setMaxY(maxYTemp);
        
        return item;
        
    }
}
