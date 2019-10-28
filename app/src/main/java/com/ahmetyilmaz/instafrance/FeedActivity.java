package com.ahmetyilmaz.instafrance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> userNameFromParse;
    ArrayList<String> userCommentFromParse;
    ArrayList<Bitmap> userImageFromParse;
    PostClass postClassAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //selecting menu for this activity -> and connect them
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.add_post){
            //intent where to go -> add post activity
            Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.logOut){ //if logOut clicked
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e!= null){ //if there any problem
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }else{//deconnection -> go back signUp activity
                        Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView=findViewById(R.id.listView1);

        userNameFromParse=new ArrayList<>();
        userCommentFromParse=new ArrayList<>();
        userImageFromParse=new ArrayList<>();

        postClassAdapter= new PostClass(userNameFromParse,userCommentFromParse,userImageFromParse,this);

        //setting custom adapter for listView
        listView.setAdapter(postClassAdapter);

        download();

    }

    public void download(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG);
                }else{//if we could download
                    if (objects.size()>0){ //if downloaded objects really charged
                        for (final ParseObject object : objects){
                            ParseFile parseFile=(ParseFile) object.get("image"); //to download image
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e==null && data != null){
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                        //if code is here it means we successfully got image -> lets get others

                                        userImageFromParse.add(bitmap);
                                        userNameFromParse.add(object.getString("username"));
                                        userCommentFromParse.add(object.getString("comment"));

                                        //alert to listener
                                        postClassAdapter.notifyDataSetChanged();



                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }
}
