package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class BluePositionDetector {
    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/bluePosition.tflite";
    private static final String[] LABELS = {
            "blue1",
            "blue2",
            "blue3"
    };

    private static final String VUFORIA_KEY = "ASAsECn/////AAABmSh4tgsgtkFHheGIzpkCKRMxnUpOSTEN6fqS1MF9IQf6lMF78O+q87jBS8VfUk+m8zKxgBjotTZ1hRbSoJeKGZ/tKwf4p0Xj9NZoRC0FrTUGFrQMVqJB8aMHgWEZe8kuwB15jX6oDfYUO6X8ZGK9egitqLn+qsJFT6A0fyGrLT/O9vhXXHdNlDes4Z/a+xyghLHcqLVV92p5nXE/cbfPIBDt27rv+ZBE3o/dMyMWj4QmnPZmFC3gPJO/gSHckSsYUQHUAlLbhWCnw/AcGwt0icjJKGmoCsZYo6eCgfp5c6ZNFxR7TcbD/SO77Xg1q+62ddMoHRS/ao9DL0v7s0q7l9dlYkvUz894Z+EwPXpzfCmf";
    private VuforiaLocalizer vuforia;
    private static int position = -1;

    private HardwareMap hwMap = null;

    private List<Recognition> updatedRecognitions = null;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    public BluePositionDetector (HardwareMap map) {
        hwMap = map;

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2.5, 16.0/9.0);
        }

        while (updatedRecognitions == null) {
            updatedRecognitions = tfod.getUpdatedRecognitions();
        }
    }

    public int getPosition() {
        if(updatedRecognitions.size() != 0){
            for (Recognition recognition : updatedRecognitions) {
                if(recognition.getLabel().equals("red1") || recognition.getLabel().equals("blue1")) return 1;
                else if(recognition.getLabel().equals("red2") || recognition.getLabel().equals("blue2")) return 2;
                else if(recognition.getLabel().equals("red3") || recognition.getLabel().equals("blue3")) return 3;
            }
        }
        return 0;
    }

    private void initVuforia() {
        // Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hwMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }
}
