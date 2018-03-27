# QrScanner
A Library for scanning qr codes based on the google vision library.

## Getting Started

Step 1: Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency:

```
dependencies {
	        compile 'com.github.Code95:C95-QR-Scanner-Android:1.0.0'
	}
```


### Usage


```
public class MainActivity extends AppCompatActivity implements OnQrScanned {

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 101;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    //Surface view used to show camera preview.
    SurfaceView surfaceView;
    //Text view to show result of qr after scanning.
    TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        resultTv = (TextView) findViewById(R.id.result_tv);

        //Initializing qr with context, SurfaceView and OnQrScanned object.
        QrReader.init(this, surfaceView, this);

        /*Qr scanner uses the camera , so the camera permission must be added to manifest and requested at runtime*/

        //Checking if camera permission is granted.
        if (ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            //Start scanning
            QrReader.scan();
        } else {
            //Request camera permission.
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION}, REQUEST_CAMERA_PERMISSION_CODE);
        }

    }

    //Called when user respond to requesting permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            //If camera permission is granted start the scanner.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Start camera preview
                QrReader.startPreview();
                //Start scanning
                QrReader.scan();
            }
        }
    }

    //Method implemented from OnQrScanned
    @Override
    public void onResult(final String barcodeValue) {
        resultTv.post(new Runnable() {
            @Override
            public void run() {
                //Setting Qr value to textView
                resultTv.setText(barcodeValue);
                //Stop scanning
                QrReader.release();
            }
        });
    }
  }
```

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```

