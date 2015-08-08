package news_analysis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import item.BorderItem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;


/**
 *
 * @author fahad_000
 */
public class ImageBorderDetectionBFS {

    static int dx[] = {0, -1, -1, -1, 0, 1, 1, 1};
    static int dy[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    
    static int d[][];
    static int width, height;

    //static BufferedImage buffImg;

    ArrayList<BorderItem> borderItems = new ArrayList<>();
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    
    public ImageBorderDetectionBFS(BufferedImage buffImg) {
        //this.buffImg = buffImg;

        //getBorder();
    }

    static long countAt;
    static int minX, maxX, minY, maxY;

    public ImageBorderDetectionBFS() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<BorderItem> getBorder(int[][] image,int width,int height) {
        int result = 0;
        this.width = width;
        this.height = height;
        this.d = image;
        //ReadFile();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                minX = 1000000;
                maxX = -1;
                minY = 1000000;
                maxY = -1;
                if (d[i][j] == 0 && image[i][j]==0) {
                    result++;
                    countAt = 1;
                    bfs(image,i, j);

                    if (countAt > 1500) {
                        canMaxiMizeBorder(minX, maxX, minY, maxY,width, height);
                        BorderItem item = new BorderItem(countAt, minX, maxX, minY, maxY, maxX-minX+1, maxY-minY+1);
                        borderItems.add(item);
                        
                    }
                }
            }
        }

        int i = 0;
        
        return borderItems;

    }

    static void bfs(int[][] image,int x, int y) {
        Queue<Integer> q = new LinkedList<Integer>();

        d[x][y] = 1;
        

        q.add(x);
        q.add(y);
        int x1, y1;
        while (!q.isEmpty()) {
            x = q.poll();
            y = q.poll();
            for (int i = 0; i < 8; i++) {
                x1 = x + dx[i];
                y1 = y + dy[i];
                if (IsIn(x1, y1) && d[x1][y1] == 0 && image[x1][y1]==0) {
                    q.add(x1);
                    q.add(y1);

                    countAt++;
                    d[x1][y1] = 1;
                    
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
    }
    static boolean IsIn(int x, int y) {
        
        return (x >= 0 && y >= 0 && x < height && y < width);
    }

    private void canMaxiMizeBorder(int minX, int maxX, int minY, int maxY, int width, int height) {
        if(minX >1 && minX < maxX-1) this.minX = minX-2;
        else if(minX >0 && minX < maxX) this.minX = minX-1;
        
        if(maxX < width -1 && maxX > minX-1 ) this.maxX = maxX+2;
        else if(maxX < width  && maxX > minX ) this.maxX = maxX+1;
        
        
        if(minY >1 && minY < maxY-1) this.minY = minY-2;
        else if(minY >0 && minY < maxY) this.minY = minY-1;
        
        if(maxY < height -1 && maxY > minY-1 ) this.maxY = maxY+2;
        else if(maxY < height  && maxY > minY ) this.maxY = maxY+1;
        
    }
    
}
