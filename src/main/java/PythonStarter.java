import java.io.IOException;

public class PythonStarter {
    public static void recognizeFace() {
        try
        {
            Process process = Runtime.getRuntime().exec("cmd /c python C:\\Users\\serws\\Desktop\\Projects\\Tester\\src\\main\\python\\FaceRecognizer.py");
            process.waitFor();
            process.destroy();
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
