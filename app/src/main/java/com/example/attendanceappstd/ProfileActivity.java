package com.example.attendanceappstd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.data.ListingAdapter;
import com.example.data.Profile;
import com.example.data.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProfileActivity extends PickImageActivity {
    protected ImageView imageView;
    ProgressBar progressBar;
    Button upload;
    TextView email;
    String userEmail;
     Uri imageUri;
FirebaseAuth auth;
FirebaseDatabase database;
DatabaseReference myRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://attendanceappstd-3a68a.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Profile");
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = auth.getCurrentUser().getEmail();
        }

        imageView = (ImageView)findViewById(R.id.imgView);
        upload = (Button) findViewById(R.id.upload);
        email = (TextView) findViewById(R.id.email);
        email.setText(userEmail);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        getDataFromServer();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null) {
                    uploadImage();
                }
            }
        });

    }

public void uploadImage(){
    StorageReference childRef = storageRef.child("images/" + generateImageTitleSimple());
    StorageMetadata metadata = new StorageMetadata.Builder()
            .setCacheControl("max-age=7776000, Expires=7776000, public, must-revalidate")
            .build();
    //uploading the image
    UploadTask uploadTask = childRef.putFile(imageUri, metadata);

    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            //myRef.child(auth.getUid()).push();
            String newNotificationId = myRef.child(auth.getUid()).push().getKey();
            Task<Uri> downloadUrll = taskSnapshot.getStorage().getDownloadUrl();

            while (!downloadUrll.isSuccessful());
            Uri downloadUrl = downloadUrll.getResult();
            Log.d("test", "successful upload image, image url: " + String.valueOf(downloadUrl));
            Profile profile = new Profile(userEmail, String.valueOf(downloadUrl));
//            profile.setEmail(userEmail);
//            profile.setURL(String.valueOf(downloadUrl));
            //myRef.child(auth.getUid()).child(newNotificationId).setValue(profile);
            myRef.child(auth.getUid()).setValue(profile);
            Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

            Toast.makeText(ProfileActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
        }
    });
}
    @Override
    protected ProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    protected ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void onImagePikedAction() {
        loadImageToImageView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = PickImageActivity.imageUri;
        Log.d("test",imageUri+"");

    }
    private void getDataFromServer() {
        myRef.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(">>>a",donCreateataSnapshot+"");
                Log.d("testing",dataSnapshot.toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                //Map<String, String> map = dataSnapshot.getValue(Map.class);
                String emailtext = (String) map.get("email");
                String url = (String) map.get("url");
                email.setText(emailtext);
                updateUI(url);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void updateUI(String url){
        if (url != null) {
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .error(R.drawable.ic_launcher_background)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            scheduleStartPostponedTransition(imageView);
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            scheduleStartPostponedTransition(imageView);
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }

    }
    private void scheduleStartPostponedTransition(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });
    }
    public static String generateImageTitleSimple() {

        return ""+ new Date().getTime();
    }
}
