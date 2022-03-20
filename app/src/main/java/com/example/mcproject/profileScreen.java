package com.example.mcproject;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class profileScreen extends Fragment {
    TextView profileScreenUserName, profileScreenContactNumber, profileScreenBloodGroup, profileScreenAddress,profileScreenPinCode;
    Button editDetailsButton;
    ImageView profilePhoto , cameraViewForImage, galleryViewForImage;
    //BottomNavigationView bmv;
    MeowBottomNavigation menuTwo;
    String currentUserImageName;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    public profileScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static profileScreen newInstance(String param1, String param2) {
        profileScreen fragment = new profileScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            View view =  inflater.inflate(R.layout.fragment_profile_screen, container, false);
            profileScreenUserName =view.findViewById(R.id.profileScreenEditText1);
            profileScreenContactNumber =view.findViewById(R.id.profileScreenEditText2);
            profileScreenBloodGroup =view.findViewById(R.id.profileScreenEditText3);
            profileScreenAddress =view.findViewById(R.id.profileScreenEditText4);
            profileScreenPinCode=view.findViewById(R.id.profileScreenEditText5);
            editDetailsButton=view.findViewById(R.id.profileScreenEditDetailButton);
            profilePhoto = view.findViewById(R.id.profieScreenimageView3);


            //bmv = getActivity().findViewById(R.id.bottomNavigationView);
            menuTwo = getActivity().findViewById(R.id.bottomNavigation);

            activeDeactiveDetails(false);

            editDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activeDeactiveDetails(true);
                    editDetailsButton.setText("Save Details");
                }
            });

            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectProfileImageOperation(/*bmv*/);
                }
            });
            return  view;
    }

    private void activeDeactiveDetails(Boolean action){
        profileScreenAddress.setEnabled(action);
        profileScreenUserName.setEnabled(action);
        profileScreenBloodGroup.setEnabled(action);
        profileScreenContactNumber.setEnabled(action);
        profileScreenPinCode.setEnabled(action);
    }

    private void selectProfileImageOperation(/*BottomNavigationView bmv*/){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        cameraViewForImage = dialogView.findViewById(R.id.imageViewADPPCamera);
        galleryViewForImage = dialogView.findViewById(R.id.imageViewADPPGallery);

        final AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);

        cameraViewForImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions()) {
                    takePictureFromCamera();
                    alertDialogProfilePicture.dismiss();
                }
            }
        });

        galleryViewForImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialogProfilePicture.dismiss();
            }
        });
    }



    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePicture, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    profilePhoto.setImageURI(selectedImageUri);
                    setImageInMenu();
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    profilePhoto.setImageBitmap(bitmapImage);
                    setImageInMenu();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        int cameraPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if(cameraPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }
        else
            Toast.makeText(getContext(), "Camera Permission not Granted", Toast.LENGTH_SHORT).show();
    }

    private void setImageInMenu(){
        Drawable profilePicDrawable = profilePhoto.getDrawable();
        //Menu navMenu = bmv.getMenu();
        Bitmap bitmap = ((BitmapDrawable)profilePicDrawable).getBitmap();

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentUserImageName = "UserImage"+" "+currentDate+" "+currentTime+".jpg";
        saveImageToFileManager(bitmap, currentUserImageName);

        File toLoad = new File(getContext().getFilesDir(), currentUserImageName);
        Glide.with(this)
                .asBitmap()
                .load(toLoad).apply(RequestOptions.circleCropTransform())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable d = new BitmapDrawable(getResources(), resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder){
                    }});
    }

    private void saveImageToFileManager(Bitmap image,String ImageName) {
        File pictureFile = new File(getActivity().getApplicationContext().getFilesDir(),ImageName);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }catch (NullPointerException e){

        }
    }
}