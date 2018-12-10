package com.holderzone.intelligencepos.mvp.model.network.errorhandling;

import android.text.TextUtils;

import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;


/**
 * Created by tcw on 2017/4/17.
 */

public class ApiException extends RuntimeException {
    private int mNoteCode = Integer.MIN_VALUE;
    private int mResultCode = Integer.MIN_VALUE;

    public ApiException(XmlData xmlData) {
        super(ApiNoteHelper.obtainErrorMsg(xmlData));
        if (xmlData != null) {
            ApiNote apiNote = xmlData.getApiNote();
            if (apiNote != null) {
                Integer noteCode = apiNote.getNoteCode();
                if (noteCode != null) {
                    mNoteCode = noteCode.intValue();
                }
                Integer resultCode = apiNote.getResultCode();
                if (resultCode != null) {
                    mResultCode = resultCode.intValue();
                }
            }
        }
    }

    public ApiException(ApiNote apiNote) {
        super(getMessage(apiNote));
        if (apiNote != null) {
            Integer noteCode = apiNote.getNoteCode();
            if (noteCode != null) {
                mNoteCode = noteCode.intValue();
            }
            Integer resultCode = apiNote.getResultCode();
            if (resultCode != null) {
                mResultCode = resultCode.intValue();
            }
        }
    }

    private static String getMessage(ApiNote apiNote) {
        if (apiNote == null) {
            return "ApiNote为空";
        }
        Integer noteCode = apiNote.getNoteCode();
        if (noteCode == null) {
            return "NoteCode为空";
        }
        String message = null;
        switch (noteCode.intValue()) {
            case 0://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -1://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -2://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -3://一定返回NoteCode&&NoteMsg，可能返回ResultCode&&ResultMsg
                message = apiNote.getResultMsg();
                if (TextUtils.isEmpty(message)) {
                    message = apiNote.getNoteMsg();
                }
                break;
            case -4://一定返回NoteCode&&NoteMsg，一定返回ResultCode&&ResultMsg
                message = apiNote.getResultMsg();
                break;
            case -100://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            case -101://只返回NoteCode&&NoteMsg
                message = apiNote.getNoteMsg();
                break;
            default:
                message = apiNote.getNoteMsg();
                break;
        }
        return message;
    }

    public int getNoteCode() {
        return mNoteCode;
    }

    public int getResultCode() {
        return mResultCode;
    }
}
