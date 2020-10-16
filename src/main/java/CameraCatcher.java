import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class CameraCatcher extends Application{
    private static Mat matrix;

    public static void main(String[] args){
        Application.launch(args);
        Detector.setImgPathIn("images/camera_test.png");
        Detector.findFace();
    }

    @Override
    public void start(Stage stage) throws IOException {
        CameraCatcher obj = new CameraCatcher();
        WritableImage writableImage = obj.captureSnapShot();
        obj.saveImage();

        ImageView imageView = new ImageView(writableImage);
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);

        Group root = new Group(imageView);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Camera catcher");
        stage.setScene(scene);
        stage.show();
    }

    public WritableImage captureSnapShot() {
        WritableImage WritableImage = null;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture(0);

        Mat matrix = new Mat();
        capture.read(matrix);

        if (capture.isOpened()) {
            if (capture.read(matrix)) {
                BufferedImage image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
                WritableRaster raster = image.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();

                byte[] data = dataBuffer.getData();
                matrix.get(0, 0, data);
                CameraCatcher.matrix = matrix;
                WritableImage = SwingFXUtils.toFXImage(image, null);
            }
        }
        return WritableImage;
    }

    public void saveImage() {
        String file = "images/camera_test.png";
        Imgcodecs imageCodecs = new Imgcodecs();
        Imgcodecs.imwrite(file, matrix);
    }
}

