package news_analysis;

import item.BorderItem;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import news_analysis.isimage.IsImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author fahad_000
 */
public class NewsAnalysis {

    static File file;
    static FileWriter fw;
    static BufferedWriter bw;

    static {
        // Load the OpenCV DLL
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        file = new File("F:\\AbcFile\\filename.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        bw.flush();
        // Load an image file and display it in a window.
        Mat m1 = Highgui.imread("H:\\error1.jpg");
        //imshow("Original", m1);

        // Do some image processing on the image and display in another window.
        Mat m2 = new Mat();
        Imgproc.bilateralFilter(m1, m2, -1, 50, 10);
        Imgproc.Canny(m2, m2, 10, 200);
        imshow("Edge Detected", m2);
        Size sizeA = m2.size();
        System.out.println("width: " + sizeA.width + " Height: " + sizeA.height);
        int width = (int) sizeA.width;
        int hight = (int) sizeA.height;
        int pointLength[][][] = new int[hight][width][2];
        for (int i = 0; i < hight; i++) {
            for (int j = 0; j < width; j++) {
                double[] data = m2.get(i, j);
                if (m2.get(i, j)[0] != 0) {
                    pointLength[i][j][0] = 0;
                    pointLength[i][j][1] = 0;
                    continue;
                }
                if (j != 0 && m2.get(i, j - 1)[0] == 0) {
                    pointLength[i][j][0] = pointLength[i][j - 1][0];
                } else {
                    int count = 0;
                    for (int k = j + 1; k < width; k++) {
                        if (m2.get(i, k)[0] == 0) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    pointLength[i][j][0] = count;
                }
                if (i != 0 && m2.get(i - 1, j)[0] == 0) {
                    pointLength[i][j][1] = pointLength[i - 1][j][1];
                } else {
                    int count = 0;
                    for (int k = i + 1; k < hight; k++) {
                        if (m2.get(k, j)[0] == 0) {
                            count++;
                        } else {
                            break;
                        }
                    }
                    pointLength[i][j][1] = count;
                }

                //System.out.println(data[0]);
            }
        }
        String temp = "";
        Mat convertArea = m2.clone();

        int[][] balckWhite = new int[hight][width];

        for (int i = 0; i < hight; i++) {
            temp = "";
            for (int j = 0; j < width; j++) {
                if (i == 0 || j == 0 || i == hight - 1 || j == width - 1) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else if (pointLength[i][j][0] > 150 && pointLength[i][j][1] > 6) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else if (pointLength[i][j][0] > 7 && pointLength[i][j][1] > 200) {
                    temp = temp + "@";
                    balckWhite[i][j] = 1;

                    double[] data = m2.get(i, j);
                    data[0] = 255.0;
                    convertArea.put(i, j, data);
                } else {
                    temp = temp + " ";
                    balckWhite[i][j] = 0;

                    double[] data = m2.get(i, j);
                    data[0] = 0.0;
                    convertArea.put(i, j, data);
                }

            }
            //filewrile(temp);
        }
        imshow("Convertion", convertArea);
        IsImage image = new IsImage();

        ImageBorderDetectionBFS imgBFS = new ImageBorderDetectionBFS();
        ArrayList<BorderItem> borderItems = imgBFS.getBorder(balckWhite, width, hight);
        Mat[] subMat = new Mat[borderItems.size()];
        for (int i = 0; i < borderItems.size(); i++) {
            subMat[i] = m2.submat(borderItems.get(i).getMinX(), borderItems.get(i).getMaxX(),
                    borderItems.get(i).getMinY(), borderItems.get(i).getMaxY());
            if (image.isImage(subMat[i])) {
                System.out.println("subMat" + i + " is an image");
                imshow("subMat" + i, subMat[i]);
                //System.out.println(borderItems.get(i).toString());
            }
            //imshow("subMat" + i, subMat[i]);
            bw.close();

        }

    }

    public void filewrile(String content) {
        try {
            bw.write(content);
            bw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void imshow(String title, Mat img) {

        // Convert image Mat to a jpeg
        MatOfByte imageBytes = new MatOfByte();
        Highgui.imencode(".jpg", img, imageBytes);

        try {
            // Put the jpeg bytes into a JFrame window and show.
            JFrame frame = new JFrame(title);
            frame.getContentPane().add(new JLabel(new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageBytes.toArray())))));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
