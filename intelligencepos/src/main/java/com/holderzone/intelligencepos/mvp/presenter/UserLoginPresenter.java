package com.holderzone.intelligencepos.mvp.presenter;

import android.util.Pair;

import com.blankj.utilcode.util.AppUtils;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.UserLoginContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.UsersE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.bean.db.Users;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.Security;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.Observable;

/**
 * 用户登录
 * Created by tcw on 2017/6/6.
 */

public class UserLoginPresenter extends BasePresenter<UserLoginContract.View> implements UserLoginContract.Presenter {

    public UserLoginPresenter(UserLoginContract.View view) {
        super(view);
    }

    @Override
    public void requestLocalAndRemoteStore() {
        // 此处用的是MerID来取门店的
        XmlData.Builder()
                .setRequestMethod(RequestMethod.StoreB.GetList)
                .setRequestBody(new StoreE()).buildRESTful()
                .flatMap(xmlData -> {
                    Observable<Store> local = mRepository.getStore();
                    Observable<XmlData> remote = mRepository.getXmlData(xmlData);
                    return Observable.zip(local, remote, Pair::new);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<Pair<Store, XmlData>>(mView) {
                    @Override
                    protected void next(Pair<Store, XmlData> storeXmlDataPair) {
                        Store first = storeXmlDataPair.first;
                        XmlData second = storeXmlDataPair.second;
                        if (ApiNoteHelper.checkBusiness(second)) {
                            mView.onRequestLocalRemoteStoreSucceed(first, second.getArrayOfStoreE());
                        } else {
                            onError(new ApiException(second));
                        }
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onRequestLocalRemoteStoreFailed();
                    }
                });
    }

    @Override
    public void requestLogin(String Number, String Password) {
        UsersE usersE = new UsersE();
        usersE.setNumber(Number);
        usersE.setPassWord(Security.md5(Password));
        usersE.setVersionCode(AppUtils.getAppVersionCode());
        usersE.setVersionName(AppUtils.getAppVersionName());
        XmlData.Builder()
                .setRequestMethod(RequestMethod.UsersB.NewLogin)
                .setRequestBody(usersE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.loginSuccess(xmlData.getUsersE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            if (apiException.getNoteCode() == -4 && apiException.getResultCode() == -30) {// -4 && -30 账号或密码错误
                                mView.loginFailOutOfAccountOrPasswordIncorrect();
                            }
                        } else {
                            mView.loginFail();
                        }
                    }
                });
    }

    @Override
    public void saveLoginUsers(Users users) {
        mRepository.saveUsers(users)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(users1 -> mView.onSaveLoginUsersSucceed());
    }
}
