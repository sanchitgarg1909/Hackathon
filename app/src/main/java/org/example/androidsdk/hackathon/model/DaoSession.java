package org.example.androidsdk.hackathon.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import org.example.androidsdk.hackathon.model.Friend;

import org.example.androidsdk.hackathon.model.FriendDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig friendDaoConfig;

    private final FriendDao friendDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        friendDaoConfig = daoConfigMap.get(FriendDao.class).clone();
        friendDaoConfig.initIdentityScope(type);

        friendDao = new FriendDao(friendDaoConfig, this);

        registerDao(Friend.class, friendDao);
    }
    
    public void clear() {
        friendDaoConfig.getIdentityScope().clear();
    }

    public FriendDao getFriendDao() {
        return friendDao;
    }

}
