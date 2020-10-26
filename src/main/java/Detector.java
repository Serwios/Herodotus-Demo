import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Detector{
    public static int countOfFaces;

    public static void findFace(String imgPathIn) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat src = Imgcodecs.imread(imgPathIn);
        System.out.println(src);
        String xmlFile = "xml/lbpcascade_frontalface.xml";
        CascadeClassifier cc = new CascadeClassifier(xmlFile);
        MatOfRect faceDetection = new MatOfRect();
        cc.detectMultiScale(src, faceDetection);
        countOfFaces = faceDetection.toArray().length;

        if(countOfFaces == 0){
            System.out.println("There is no faces");
        }else {
            if (countOfFaces == 1){
                System.out.println("There is one face");
            }else{
            System.out.println("There is " + faceDetection.toArray().length + " faces: ");
            }
        }

         for (Rect rect: faceDetection.toArray()) {
             Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0,0,255), 10);
         }

        String imgPathOut = "images/test_out.png";
        Imgcodecs.imwrite(imgPathOut, src);
        System.out.println("\n [[Detection finished]]");

    }
}

