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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.RemoteMessage;
import com.zoom.happiestplaces.R;
import com.zoom.happiestplaces.databinding.ScanFragmentBinding;
import com.zoom.happiestplaces.util.AppConstants;
import com.zoom.happiestplaces.util.AppUtils;
import com.zoom.happiestplaces.util.OrderUtils;
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
    Boolean scanned;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       if(!SharedPrefUtils.getFirstTimeUser(getActivity().getApplicationContext())) {
         SharedPrefUtils.setFirstTimeUser(getActivity().getApplicationContext(),true);
           NavHostFragment.findNavController(getParentFragment()).navigate(R.id.introViewPagerFragment);

       }
       else
        onNewIntent();
    }
    //Handle Push notifications if the app is in background
    public void onNewIntent() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        String str=intent.getDataString();
        if (bundle != null) {
            if (bundle.containsKey(AppConstants.KEY_ORDER_STATUS)) {
                String status = bundle.getString(AppConstants.KEY_ORDER_STATUS);
                if (status.equals(AppConstants.Status.Paid.toString())) {
                    String orderId = bundle.getString(AppConstants.KEY_ORDER_ID);
                    String restId = bundle.getString(AppConstants.KEY_RESTAURANT_ID);
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.addReviewFragment, OrderUtils.getOrderRestoBundle(UUID.fromString(orderId), UUID.fromString(restId)));
                } else {
                   // Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.customerOrdersFragment);
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.customerOrdersFragment);}
                }
            else
                if(bundle.containsKey(AppConstants.KEY_CUSTOMER)){
                    if(SharedPrefUtils.getCustomer(getActivity().getApplicationContext())!=null)
                    {
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.profileFragment);
                    }

                }
            }

        FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getActivity().getIntent())
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                try {
                                    String custID=deepLink.getQueryParameter(AppConstants.KEY_INVITED);
                                    if(deepLink.getBooleanQueryParameter(
                                            AppConstants.KEY_INVITED,false)) {
                                        custID=deepLink.getQueryParameter(AppConstants.KEY_INVITED);
                                        if(!TextUtils.isEmpty(custID)) {
                                            SharedPrefUtils.saveReferral(getContext().getApplicationContext(),custID);
                                            //mViewModel.saveReferral(custID);

                                        }
                                    }
                                }catch (Exception e){
                                    Log.d(AppConstants.TAG, " error "+e.toString());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(AppConstants.TAG, "getDynamicLink:onFailure", e);
                        }
                    });

    }


        @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ScanFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        mPreviewView = mBinding.previewView;
        mViewModel.getRegistrationToken();
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
        scanned=false;
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
        mAnalyzer = new MyImageAnalyzer();
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CAMERA) != (PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                Manifest.permission.CAMERA}, 101);
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

        ImageCapture imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 728))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, mAnalyzer);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    public class MyImageAnalyzer implements ImageAnalysis.Analyzer {
        private FragmentManager fragmentManager;

        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanbarcode(image);
        }

        private void scanbarcode(ImageProxy imageProxy) {
            @SuppressLint("UnsafeOptInUsageError") Image image1 = imageProxy.getImage();
            assert imageProxy != null;
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
            for (Barcode barcode : barcodes) {
                Rect bounds = barcode.getBoundingBox();
                Point[] corners = barcode.getCornerPoints();
                String rawValue = barcode.getRawValue();
                int valueType = barcode.getValueType();
                // See API reference for complete list of supported types
                if(!scanned)
                switch (valueType) {
                    case Barcode.TYPE_TEXT:
                        String table = barcode.getDisplayValue();
                        showRestaurantMenu(table);
                        break;
                    case Barcode.TYPE_URL:
                        String url = barcode.getDisplayValue();
                        if(QRUtils.validateQRCODEURL(url))
                            showRestaurantMenu(QRUtils.get_table_id(url));
                        else
                            AppUtils.showSnackbar(getParentFragment().getView(), getString(R.string.wrongQR));
                        break;
                    default:
                        scanned=false;
                        AppUtils.showSnackbar(getParentFragment().getView(), getString(R.string.wrongQR));
                }
            }
        }
    }

    private void showRestaurantMenu(String QRcode) {
        scanned=true;
        try {
            UUID mQRcode = UUID.fromString(QRcode);
            SharedPrefUtils.createOrder(getActivity().getApplicationContext(), mQRcode);
        } catch (IllegalArgumentException exception) {

            // Toast.makeText(getContext(),getString(R.string.wrongQR),Toast.LENGTH_SHORT).show();
        }
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.foodMenuFragment,
                RestaurantUtils.getQRBundle(QRcode)
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}