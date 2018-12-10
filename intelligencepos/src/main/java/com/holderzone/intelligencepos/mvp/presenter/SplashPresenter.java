package com.holderzone.intelligencepos.mvp.presenter;

import android.text.TextUtils;
import android.util.Pair;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SplashContract;
import com.holderzone.intelligencepos.mvp.model.bean.AdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.PaymentItemE;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterConfigE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SysParametersE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.printer.PushPrintResponse;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 启动页 数据缓存
 * Created by tcw on 2017/3/20.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    public SplashPresenter(SplashContract.View view) {
        super(view);
    }

    @Override
    public void requestData() {
        requestDishType()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return mRepository.saveAllDishesTypeE(xmlData.getArrayOfDishesTypeE());
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(xmlData -> mView.refreshUI(25))
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> requestSystemConfig())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return mRepository.saveSystemConfig(xmlData.getParametersConfig());
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .flatMap(xmlData -> pushRegister())
                .flatMap(pushPrintResponse -> {
                    if (pushPrintResponse.getSuccess() == 1) {
                        return requestDish();
                    }
                    return Observable.error(new Exception("推送设备注册失败"));
                })
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return mRepository.saveAllDishesE(xmlData.getArrayOfDishesE());
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> requestSysParameters())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        SysParametersE sysParametersE = xmlData.getSysParametersE();
                        if (sysParametersE != null && 1 == sysParametersE.getState()) {
                            return mRepository.saveIsDesignatedDishes(true);
                        } else {
                            return mRepository.saveIsDesignatedDishes(false);
                        }
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(xmlData -> mView.refreshUI(50))
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> requestAdditionalFeesE())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        List<AdditionalFeesE> arrayOfAdditionalFeesE = xmlData.getArrayOfAdditionalFeesE();
                        List<AdditionalFees> arrayOfAdditionalFees = new ArrayList<>();
                        for (AdditionalFeesE additionalFeesE : arrayOfAdditionalFeesE) {
                            arrayOfAdditionalFees.add(additionalFeesE);
                        }
                        return mRepository.saveAllAdditionalFee(arrayOfAdditionalFees);
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(xmlData -> mView.refreshUI(75))
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> requestAllPaymentItem())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        List<PaymentItemE> arrayOfPaymentItemE = xmlData.getArrayOfPaymentItemE();
                        List<PaymentItem> arrayOfPaymentItem = new ArrayList<>();
                        for (PaymentItemE paymentItemE : arrayOfPaymentItemE) {
                            arrayOfPaymentItem.add(paymentItemE);
                        }
                        return mRepository.saveAllPaymentItem(arrayOfPaymentItem);
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(xmlData -> requestPrintLogoPictures())
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        List<String> logoList = new ArrayList<>();
                        for (PrinterConfigE configE : xmlData.getArrayOfPrinterConfigE()) {
                            logoList.add(configE.getImgLogo());
                        }
                        return Observable.fromIterable(logoList);
                    }
                    return Observable.error(new ApiException(xmlData));
                })
                .flatMap(remoteUrl -> {
                    return downloadThenSavePictures(remoteUrl, ImageBean.Type.PrintLogo);
                })
                .toList()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(new BaseObserver<List<Boolean>>(mView) {
                    @Override
                    protected void next(List<Boolean> paymentItemList) {
                        mView.refreshUI(100);
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.showErrorLayout();
                    }
                });
    }

    private Observable<XmlData> requestPrintLogoPictures() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.PrinterConfigB.GetPrintLogoList)
                .setRequestBody(new PrinterConfigE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    private Observable<Boolean> downloadThenSavePictures(String remoteUrl, ImageBean.Type type) {
        return verifyRemoteUrlInDbExist(remoteUrl, type);
    }

    private Observable<Boolean> verifyRemoteUrlInDbExist(String remoteUrl, ImageBean.Type type) {
        return mRepository.checkRemoteUrlInDbExist(remoteUrl)
                .flatMap(localPath -> {
                    if (!TextUtils.isEmpty(localPath)) {// 数据库已存在该记录
                        return verifyLocalFileInDiskExist(localPath, remoteUrl, type);
                    } else {// 数据库不存在该记录
                        return excuteDownload(remoteUrl, type);
                    }
                });
    }

    private Observable<Boolean> verifyLocalFileInDiskExist(String localPath, String remoteUrl, ImageBean.Type type) {
        return mRepository.checkLocalFileInDiskExist(localPath)
                .flatMap(aBoolean -> {
                    if (aBoolean) {// disk存在该路径的对应的文件
                        return prepareNext();
                    } else {// disk不存在该路径的对应的文件
                        return excuteDownload(remoteUrl, type);
                    }
                });
    }

    private Observable<Boolean> excuteDownload(String remoteUrl, ImageBean.Type type) {
        return mRepository.downloadPicture(remoteUrl)
                .flatMap(responseBody -> writeFileToDisk(remoteUrl, responseBody, type));
    }

    private Observable<Boolean> prepareNext() {
        return Observable.just(true);
    }

    private Observable<XmlData> requestDishType() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesTypeB.GetList)
                .setRequestBody(new DishesTypeE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    private Observable<PushPrintResponse> pushRegister() {
        return Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(), Pair::new)
                .flatMap(pair -> mRepository.pushRegister(pair.first.getEnterpriseInfoGUID(), pair.second.getStoreGUID()));
    }

    private Observable<Boolean> writeFileToDisk(String remoteUrl, ResponseBody responseBody, ImageBean.Type type) {
        return mRepository.writeFileToDisk(remoteUrl, responseBody, type)
                .flatMap(localUrl -> {
                    if (!TextUtils.isEmpty(localUrl)) {// 写入成功
                        return mRepository.checkRemoteUrlInDbExist(remoteUrl)
                                .flatMap(s -> {
                                    if (TextUtils.isEmpty(s)) {
                                        ImageBean imageBean = new ImageBean();
                                        imageBean.setRemoteUrl(remoteUrl);
                                        imageBean.setType(type.getType());
                                        imageBean.setLocalUrl(localUrl);
                                        return insertImageBeanToDb(imageBean);
                                    } else {
                                        ImageBean imageBean = new ImageBean();
                                        imageBean.setRemoteUrl(remoteUrl);
                                        imageBean.setLocalUrl(localUrl);
                                        return updateImageBeanToDb(imageBean);
                                    }
                                });
                    } else {// 写入失败
                        return prepareNext();
                    }
                });
    }

    private Observable<Boolean> insertImageBeanToDb(ImageBean imageBean) {
        return mRepository.insertImageBeanToDb(imageBean)
                .flatMap(aBoolean -> prepareNext());
    }

    private Observable<Boolean> updateImageBeanToDb(ImageBean imageBean) {
        return mRepository.updateImageBeanToDb(imageBean)
                .flatMap(aBoolean -> prepareNext());
    }

    private Observable<XmlData> requestDish() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesB.GetList)
                .setRequestBody(new DishesE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    private Observable<XmlData> requestSysParameters() {
        SysParametersE sysParametersE = new SysParametersE();
        sysParametersE.setParType(2);
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.SysParametersB.GetParameters)
                .setRequestBody(sysParametersE).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    private Observable<XmlData> requestAdditionalFeesE() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.AdditionalFeesB.GetList)
                .setRequestBody(new AdditionalFeesE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    private Observable<XmlData> requestAllPaymentItem() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.PaymentItemB.GetList)
                .setRequestBody(new PaymentItemE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    /**
     * 请求开关实体集合
     */
    private Observable<XmlData> requestSystemConfig() {
        SysParametersE sysParametersE = new SysParametersE();
        sysParametersE.setParType(2);
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.SysParametersB.getSystemConfig)
                .setRequestBody(sysParametersE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData);
    }
}
