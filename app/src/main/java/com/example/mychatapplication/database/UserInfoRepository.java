package com.example.mychatapplication.database;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.NavigationActivity;
import com.example.mychatapplication.WebSocketClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UserInfoRepository {
    private UserInfoDao userInfoDao;
    private static String insertUpdateCode = "0";

    public UserInfoRepository(Context context) {
        UserInfoDatabase userInfoDatabase = UserInfoDatabase.getDatabase(context.getApplicationContext());
        userInfoDao = userInfoDatabase.getUserInfoDao();
    }

    public LiveData<List<UserInfo>> getAllUserInfoLiveData() {
        return userInfoDao.getAllUserInfoLiveData();
    }

    public List<UserInfo> getAllUserInfo() {
        return userInfoDao.getAllUserInfo();
    }

    public LiveData<UserInfo> getCurrentUserInfoLiveData(String jyId) {
        return userInfoDao.getUserInfoLiveData(jyId);
    }

    public UserInfo getCurrentUserInfo(String jyId) {
        return userInfoDao.getUserInfo(jyId);
    }

    public void insertUserInfo(UserInfo... userInfos) {
        new InsertAsyncTask(userInfoDao).execute(userInfos);
    }

    public void updateUserInfo(UserInfo... userInfos) {
        new UpdateAsyncTask(userInfoDao).execute(userInfos);
    }

    public void updateItem(String jyId, String item, String value) {
        UpdateItemThread updateItemThread = new UpdateItemThread(userInfoDao, jyId, item, value);
        new Thread(updateItemThread).start();
    }

    public InsertUpdate insertUpdate(UserInfo... userInfo) {
        return new InsertUpdate(userInfoDao, userInfo);
    }

    public boolean userInfoExist(String jyid){
        return userInfoDao.userExist(jyid);
    }

    public class InsertUpdate {
        private UserInfo[] userInfos;
        private ArrayList<UserInfo> userInfoArrayList;
        private UserInfoDao userInfoDao;

        public InsertUpdate(UserInfoDao userInfoDao, UserInfo[] userInfos) {
            this.userInfos = userInfos;
            this.userInfoDao = userInfoDao;
            this.userInfoArrayList = new ArrayList<>(Arrays.asList(userInfos));
        }

        public String insertUpdate() {
            if(insertUpdateCode.equals("0")){
                insertUpdateCode = "1";
                if (userInfoArrayList.size() != 0) {
                    CompletableFuture.supplyAsync(() -> userInfoDao.userExist(userInfoArrayList.get(0).getJyId())
                    ).thenApply(exist -> {
                        Log.d("判决", exist ? userInfoArrayList.get(0).getJyId() + "存在room" : userInfoArrayList.get(0).getJyId() + "不存在room");
                        if (exist) {
                            Map<String, String> notNoneFields = userInfoArrayList.get(0).notNoneFields();
                            for (String key : notNoneFields.keySet()) {
                                Log.d("判决", "存在字段" + key);
                                updateItem(userInfoArrayList.get(0).getJyId(), key, notNoneFields.get(key));
                            }
                        } else {
                            Log.d("判决", "不存在直接插入");
                            userInfoDao.insertUserInfo(userInfoArrayList.get(0));
                        }
                        return "1";
                    }).thenAccept(result -> {
                        userInfoArrayList.remove(0);
                        insertUpdate();
                        insertUpdateCode = "0";
                        WebSocketClass.getInsertUpdateLinkedListMutableLiveData().getValue().removeFirst();
                        WebSocketClass.getInsertUpdateLinkedListMutableLiveData().postValue(WebSocketClass.getInstance().getInsertUpdateLinkedListMutableLiveData().getValue());
                    });
                } else {
                    Log.d("插入更新", "全部结束");
                }
                return "0";
            }else{
                return "1";
            }
        }
    }


    static class InsertAsyncTask extends AsyncTask<UserInfo, Void, Void> {
        private UserInfoDao userInfoDao;

        public InsertAsyncTask(UserInfoDao userInfoDao) {
            this.userInfoDao = userInfoDao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            for (UserInfo userInfo : userInfos) {
                if (userInfoDao.getUserInfo(userInfo.getJyId()) == null) {
                    Log.d("数据库插入操作", "插入ID" + userInfo.getJyId() + "不存在");
                    userInfoDao.insertUserInfo(userInfos);
                } else {
                    Log.d("数据库插入操作", "插入ID" + userInfo.getJyId() + "已存在");
                }
            }

            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<UserInfo, Void, Void> {
        private UserInfoDao userInfoDao;

        public UpdateAsyncTask(UserInfoDao userInfoDao) {
            this.userInfoDao = userInfoDao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            userInfoDao.updateUserInfo(userInfos);
            return null;
        }
    }

    static class UpdateItemThread implements Runnable {
        private UserInfoDao userInfoDao;
        private String jyId, item, value;

        public UpdateItemThread(UserInfoDao userInfoDao, String jyId, String item, String value) {
            this.userInfoDao = userInfoDao;
            this.jyId = jyId;
            this.item = item;
            this.value = value;
        }

        @Override
        public void run() {
            switch (item) {
                case "avatar":
                    userInfoDao.updateAvatar(jyId, value);
                    break;
                case "nickname":
                    userInfoDao.updateNickname(jyId, value);
                    break;
                case "sex":
                    userInfoDao.updateSex(jyId, value);
                    break;
                case "area":
                    userInfoDao.updateArea(jyId, value);
                    break;
                case "friendChatId":
                    userInfoDao.updateFriendChatId(jyId, value);
                    break;
                case "friendChatMessages":
                    userInfoDao.updateFriendChatMessages(jyId, value);
                    break;
                default:
                    Log.d("更新数据失败", item);
            }
        }
    }

//    public static class QueryUserExistWork extends Worker{
//        private WorkerParameters inputData;
//        public QueryUserExistWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//            super(context, workerParams);
//        }
//
//        @NonNull
//        @Override
//        public Result doWork() {
//            UserInfoDatabase userInfoDatabase = UserInfoDatabase.getDatabase(MainApplication.getInstance().getApplicationContext());
//            UserInfoDao userInfoDao = userInfoDatabase.getUserInfoDao();
//
//            String item = getInputData().getString("item");
//            String source = getInputData().getString("source");
//            Boolean isExist =  Boolean.valueOf(userInfoDao.userExist(source));
//
//            Data outputData = new Data.Builder()
//                    .putBoolean("isExist", isExist)
//                    .build();
//            return Result.success();
//        }
//    }

//    public static class InsertUpdate extends Worker{
//        private WorkerParameters inputData;
//        public InsertUpdate(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//            super(context, workerParams);
//        }
//
//        @NonNull
//        @Override
//        public Result doWork() {
//            UserInfoDatabase userInfoDatabase = UserInfoDatabase.getDatabase(MainApplication.getInstance().getApplicationContext());
//            UserInfoDao userInfoDao = userInfoDatabase.getUserInfoDao();
//
//            String item = getInputData().getString("item");
//            String source = getInputData().getString("source");
//
//            switch(Objects.requireNonNull(item)){
//                case "avatar":
//                    byte[] imgBytes = getInputData().getByteArray("value");
//                    userInfoDao.updateAvatar(source, new String(imgBytes));
//                    break;
//                case "nickname":
//                    String nickname = getInputData().getString("value");
//                    userInfoDao.updateNickname(source, nickname);
//                    break;
//                default:
//                    Log.d("不存在", item);
//            }
//
//            return Result.success();
//        }
//    }
}
