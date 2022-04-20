package com.zoom.happiestplaces.scan;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.ScanFragmentBinding;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.QRUtils;
import com.zoom.happiestplaces.util.RestaurantUtils;
import com.zoom.happiestplaces.util.SharedPrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanFragment extends Fragment {
    private ScanViewModel mViewModel;
    private ScanFragmentBinding mBinding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor cameraExecutor = Executors.newSingleThreadExecutor();
    MyImageAnalyzer mAnalyzer;
    PreviewView mPreviewView;
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding=ScanFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
       mPreviewView= mBinding.previewView;
     //   startCamera();
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Asks runtime permissions
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initCamera() {
        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initCamera();
    }
    private boolean allPermissionsGranted() {//fn checks whether user has necessary permissions
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private void startCamera() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        mAnalyzer=new MyImageAnalyzer();
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if( ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CAMERA)!= (PackageManager.PERMISSION_GRANTED))
                    {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{
                                Manifest.permission.CAMERA},101);
                    }
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }
    void bindPreview(@NonNull ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageCapture imageCapture=new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis=new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280,728))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor,mAnalyzer);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture,imageAnalysis);
    }

    public class MyImageAnalyzer implements ImageAnalysis.Analyzer{
        private FragmentManager fragmentManager;
        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanbarcode(image);
        }
        private void scanbarcode(ImageProxy imageProxy){
            @SuppressLint("UnsafeOptInUsageError") Image image1= imageProxy.getImage();
            assert imageProxy!=null;
            InputImage inputImage =
                    InputImage.fromMediaImage(image1, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API
            BarcodeScannerOptions options =
                    new BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(
                                    Barcode.FORMAT_QR_CODE,
                                    Barcode.FORMAT_AZTEC)
                            .build();
            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            Task<List<Barcode>> result = scanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            readBarcodeData(barcodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                        }

                    })
                    .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            imageProxy.close();
                        }
                    });

        }

        private void readBarcodeData(List<Barcode> barcodes) {
            for (Barcode barcode: barcodes) {
                Rect bounds = barcode.getBoundingBox();
                Point[] corners = barcode.getCornerPoints();

                String rawValue = barcode.getRawValue();

                int valueType = barcode.getValueType();
                // See API reference for complete list of supported types
                switch (valueType) {
                    case Barcode.TYPE_TEXT:
                        String table = barcode.getDisplayValue();
                        showRestaurantMenu(table);
                        break;
                    default:AppUtils.showSnackbar(getView(),getString(R.string.wrongQR));
                }
            }
        }
    }

    private void showRestaurantMenu(String QRcode) {
        //TODO:if the qr code is wrong
        Log.d(AppConstants.TAG,"Qr code "+QRcode.toString());
        NavHostFragment.findNavController(this).navigate(R.id.foodMenuFragment,
                RestaurantUtils.getQRBundle(QRcode)
        );
       /* if (QRUtils.checkQRFormat(QRcode))
        {

            mViewModel.getRestaurant(QRUtils.getRestaurantIdQR(QRcode)).observe(
                    this, restaurant -> {
                if(restaurant==null) {
                    if(!AppUtils.isNetworkAvailableAndConnected(getContext()))
                        AppUtils.showSnackbar(getView(),getString(R.string.network_err));
                   // AppUtils.showSnackbar(getView(),getString(R.string.wrongRest));
                }
                else {
                    SharedPrefUtils.createOrder(getActivity().getApplicationContext(),QRUtils.getTableQR(QRcode),restaurant);
                    NavHostFragment.findNavController(this).navigate(R.id.foodMenuFragment,
                            RestaurantUtils.getRestaurantBundle(QRUtils.getRestaurantIdQR(QRcode)));
                }
            });
        }
        else
        {
            AppUtils.showSnackbar(getView(),getString(R.string.wrongQR));
        }*/
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}