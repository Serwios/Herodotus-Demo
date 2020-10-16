import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Detector{
    private static String imgPathIn;
    private static String imgPathOut;

    public static void findFace() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        imgPathIn = "images/camera_test.png";
        Mat src = Imgcodecs.imread(imgPathIn);
        String xmlFile = "xml/lbpcascade_frontalface.xml";
        CascadeClassifier cc = new CascadeClassifier(xmlFile);

        MatOfRect faceDetection = new MatOfRect();
        cc.detectMultiScale(src, faceDetection);
        System.out.printf("Detected faces: %n", faceDetection.toArray().length);

        for (Rect rect: faceDetection.toArray()) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0,0,255), 10);
        }

        imgPathOut = "images/test_out.png";
        Imgcodecs.imwrite(imgPathOut, src);
        System.out.println("Detection finished");

    }

    public static String getImgPathIn() {
        return imgPathIn;
    }

    public static void setImgPathIn(String imgFileIn) {
        Detector.imgPathIn = imgFileIn;
    }

    public static String getImgFileOut() {
        return imgPathOut;
    }

    public static void setImgFileOut(String imgFileOut) {
        Detector.imgPathOut = imgFileOut;
    }
}

