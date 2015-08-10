/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news_analysis.headlinedetection;

import item.BorderItem;
import java.util.ArrayList;
import news_analysis.NewsAnalysis;
import news_analysis.isimage.BorderDetection;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 *
 * @author fahad_000
 */
public class HeadLineDetection {
    
    public boolean isHeadLine(Mat image){
        Size imageSize = image.size();
        int width = image.width();
        int height = image.height();
        
        if(height >50 && height <100) {
            return horizontalChecked( image, width,  height);
        }
        //VerticleChecked( image, width,  height);
        
        return false;      
    }
    
    private boolean horizontalChecked(Mat image,int width, int height) {
        int countHisto=0;          
        int countLine=0;
        int maxTAG=-1;
        int minTAG=100000;
        
        int freq[] = new int[height];
        for(int i=0; i< height;i++){  
            countHisto=0;
            for(int j=0; j< width ; j++){
                if(image.get(i, j)[0] == 255){                    
                    countHisto++;
                   //System.out.print("*");
                }
                
            }
            freq[i] = countHisto;
            if(freq[i]>maxTAG) maxTAG=freq[i];
            if(freq[i]<minTAG) minTAG=freq[i];
            if(freq[i]==0) countLine++;
            //System.out.println("Line: "+countLine);
        }
        
        boolean any = true;
        for(int i=0; i<height*.3;i++){
            if(freq[i]==maxTAG){
                for(int j=freq[height-1];j>height*.9;j--){
                    if(freq[i]<=minTAG+5 ){
                        
                    }else{
                        any = false;
                    }
                }
            }
        }
        
        if(countLine<4) return any;
        
        return false;
        
    }
    private int VerticleChecked(Mat image,int width, int height) {
        
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
        
        //System.out.println("start_H_HV : "+start_H_HV+" End_H_HV : "+End_H_HV);
        
        return 0;
        
    }
}
