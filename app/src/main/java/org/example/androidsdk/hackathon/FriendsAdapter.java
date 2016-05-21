package org.example.androidsdk.hackathon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.example.androidsdk.hackathon.model.Friend;
import org.example.androidsdk.hackathon.model.FriendDao;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.PlacesViewHolder> {

    private List<Friend> friendList;
    Context context;
    FriendDao friendDao;

    public FriendsAdapter(Context context, List<Friend> friendList, FriendDao friendDao) {
        this.friendList = friendList;
        this.context = context;
        this.friendDao = friendDao;
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected CheckBox checkBox;
        protected String id;

        public PlacesViewHolder(View convertView) {
            super(convertView);
            name = (TextView) convertView.findViewById(R.id.friend_name);
            checkBox = (CheckBox) convertView.findViewById(R.id.friend_checkbox);
        }
    }

    public void update(List<Friend> arrayList){
        this.friendList=arrayList;
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder viewHolder, final int position) {
        Friend friend = friendList.get(position);
        viewHolder.name.setText(friend.getName());
        viewHolder.id = friend.getFbId();
        viewHolder.checkBox.setChecked(friend.getIsAdded());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.checkBox.isChecked())
                    addFriend(viewHolder.id,position,viewHolder.checkBox);
                else
                    removeFriend(viewHolder.id,position,viewHolder.checkBox);
            }
        });
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list_item, viewGroup, false);
        return new PlacesViewHolder(itemView);
    }

    public void addFriend(String fbId, final int position, final CheckBox c){
        ServerRequest serverRequest = new ServerRequest(context);
        serverRequest.addFriend(fbId, new GetResponseCallback() {
            @Override
            public void callback(String resp) {
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject response = new JSONObject(resp);
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
                        Friend friend = friendList.get(position);
                        friend.setIsAdded(true);
                        friendDao.insertOrReplace(friend);
                    }
                    else{
                        Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                        c.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeFriend(String fbId, final int position, final CheckBox c){
        ServerRequest serverRequest = new ServerRequest(context);
        serverRequest.removeFriend(fbId, new GetResponseCallback() {
            @Override
            public void callback(String resp) {
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject response = new JSONObject(resp);
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
                        Friend friend = friendList.get(position);
                        friend.setIsAdded(false);
                        friendDao.insertOrReplace(friend);
                    }
                    else{
                        Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                        c.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}