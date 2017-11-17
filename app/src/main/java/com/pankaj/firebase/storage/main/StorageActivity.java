package com.pankaj.firebase.storage.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pankaj.firebase.storage.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StorageActivity extends AppCompatActivity {

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.txtUpload)
    Button txtUpload;

    FirebaseStorage firebaseStorage;
    StorageReference firebaseStorageRef;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivUpload)
    ImageView ivUpload;
    @BindView(R.id.txtChooseImage)
    Button txtChooseImage;

    private int RESULT_LOAD_IMG = 310;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorageRef = firebaseStorage.getReferenceFromUrl(FireConstant.fireDatabaseServer);


        StorageReference currentFile = firebaseStorageRef.child("chess" + ".jpg");
//        Glide.with(this)
//                .using(new FirebaseImageLoader())
//                .load(currentFile)
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);

        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(currentFile)
                .crossFade()
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ivUpload.destroyDrawingCache();
                ivUpload.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(StorageActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(StorageActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.txtChooseImage)
    void performChooseAction(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @OnClick(R.id.txtUpload)
    public void performUploadImageAction() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/"+ts+".jpg");
        firebaseStorageRef.getName().equals(mountainImagesRef.getName());    // true
        firebaseStorageRef.getPath().equals(mountainImagesRef.getPath());    // false

        ivUpload.setDrawingCacheEnabled(true);
        ivUpload.buildDrawingCache();
        Bitmap bitmap = ivUpload.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StorageActivity.this, downloadUrl.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
