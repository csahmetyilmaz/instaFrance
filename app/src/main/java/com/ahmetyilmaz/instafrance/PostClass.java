package com.ahmetyilmaz.instafrance;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {
    //This class made for getting an arrayAdapter between feed activity and listView with CustomView..

    private final ArrayList<String> userName;
    private final ArrayList<String> userComment;
    private final ArrayList<Bitmap> userImage;
    private final Activity context;

    public PostClass(ArrayList<String> userName,ArrayList<String> userComment,ArrayList<Bitmap> userImage,Activity context){
       super(context,R.layout.custom_view,userName);//connecting arraylistthis.userName=userName;
       this.userName=userName;
       this.userComment=userComment;
       this.userImage=userImage;
       this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        //getting customView as object
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);

        TextView userNameText = customView.findViewById(R.id.custom_view_username_text);
        TextView userCommentText = customView.findViewById(R.id.custom_view_comment_text);
        ImageView imageView = customView.findViewById(R.id.custom_view_image_view);

        userNameText.setText(userName.get(position));
        userCommentText.setText(userComment.get(position));
        imageView.setImageBitmap(userImage.get(position));

        return customView;
    }
}
