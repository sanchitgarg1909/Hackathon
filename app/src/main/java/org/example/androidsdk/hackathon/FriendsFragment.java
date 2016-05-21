package org.example.androidsdk.hackathon;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.example.androidsdk.hackathon.model.DaoMaster;
import org.example.androidsdk.hackathon.model.DaoSession;
import org.example.androidsdk.hackathon.model.Friend;
import org.example.androidsdk.hackathon.model.FriendDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private List<Friend> friendList = new ArrayList<>();
    FriendsAdapter adapter;
    DaoSession daoSession;
    FriendDao friendDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        FacebookSdk.sdkInitialize(getContext());
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        friendDao = daoSession.getFriendDao();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        friendList = friendDao.loadAll();
        adapter = new FriendsAdapter(getActivity(),friendList,friendDao);
        recyclerView.setAdapter(adapter);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("friends", response.toString());
                        JSONObject object = response.getJSONObject();
                        if(object!=null) {
                            try {
                                friendDao.deleteAll();
                                JSONArray data = object.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);
                                    Friend friend = new Friend(jsonObject.getString("name"), jsonObject.getString("id"),true);
                                    friendDao.insertOrReplace(friend);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            friendList = friendDao.loadAll();
                            Toast.makeText(getActivity(),friendList.size()+"",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "Token expired", Toast.LENGTH_SHORT).show();
                            friendList = friendDao.loadAll();
                            Toast.makeText(getActivity(),friendList.size()+"",Toast.LENGTH_SHORT).show();
                        }
                        adapter.update(friendList);
                        adapter.notifyDataSetChanged();
                    }
                }
        ).executeAsync();
        Button invite = (Button) rootView.findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLinkUrl, previewImageUrl;

                appLinkUrl = "https://www.mydomain.com/myapplink";
                previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(getActivity(), content);
                }
            }
        });
        return rootView;
    }

}
