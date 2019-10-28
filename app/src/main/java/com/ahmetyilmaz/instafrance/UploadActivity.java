package com.ahmetyilmaz.instafrance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    EditText commentText;
    ImageView imageView;
    Bitmap chosenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //assign text and image
        commentText=findViewById(R.id.uploadActivityCommentText);
        imageView=findViewById(R.id.uploadActivityImageView);

    }

    public void postUpload(View view){

        String comment =commentText.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        chosenImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes =byteArrayOutputStream.toByteArray();


        ParseFile parseFile=new ParseFile("image.png",bytes);


        ParseObject object=new ParseObject("Posts");

        object.put("image",parseFile);
        object.put("comment",comment);
        object.put("username", ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){// if there any error
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG);

                }else{//if all is well -> post uploaded -> send to feed activity
                    Toast.makeText(getApplicationContext(),"Post Uploaded!",Toast.LENGTH_LONG);
                    Intent intent =new Intent(getApplicationContext(),FeedActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    public void choseImage(View view){ // select image from gallery
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //if there is no permission yet request it
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);

            //if i got permission now what to do -> it will be in onRequestPermissions


        }else{
            //if it is permitted go to gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);

            //if i got image uri what to do -> it will be in onActivityResult
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if (requestCode==2){
           if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               //if it is permitted go to gallery
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(intent,1);
           }
       }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //if i got image uri what to do
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            // if i got image data as we want -> get Image Bitmap
            Uri imageData=data.getData();
            try {
                //version control to resolve getBitmap deprecate problem.
                if (Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),imageData);
                    chosenImage=ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(chosenImage);
                }else{
                    chosenImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(chosenImage);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }




        super.onActivityResult(requestCode, resultCode, data);
    }
}
