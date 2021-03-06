package com.util.emotions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtil {

    public static final String ASSET_EMOJI_FOLDER = "emojis";
    //delete icon
    public static final String BACKSPACE = "backspace";
    private static EmojiUtil mInstance;
    private Context mContext;
    //eg.emj_001
    private String[] emojiIconNameArray;

    public EmojiUtil(Context context) {
        mContext = context;
        try {
            String[] iconsArray = context.getAssets().list(ASSET_EMOJI_FOLDER);
            //remove "delete icon"
            emojiIconNameArray = new String[iconsArray.length - 1];
            int removeBackPosition = 0;
            for (int j = 0; j < iconsArray.length; j++) {
                String fileName = iconsArray[j].substring(0, iconsArray[j].indexOf("."));
                if (BACKSPACE.equals(fileName)) {
                    removeBackPosition = 1;
                } else {
                    emojiIconNameArray[j - removeBackPosition] = fileName;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EmojiUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new EmojiUtil(context);
        }

        return mInstance;
    }

    public String[] getEmojiNameArray() {
        return emojiIconNameArray;
    }

    public Bitmap getEmojiIcon(String iconName) {
        Bitmap bmp = null;
        InputStream inputStream = null;
        try {
            inputStream = mContext.getAssets().open(ASSET_EMOJI_FOLDER + File.separator + iconName + ".png");
            bmp = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmp;
    }

    /**
     * 根据emoji名字（如：emji_001）
     */
    public SpannableString getSpannableByEmojiName(Context context,
                                                   String iconName) {
        //将文件名组装成assets中对应的文件名(不包括扩展名)
        if (iconName.contains("[") && (iconName.contains("]"))) {
            iconName = iconName.substring(iconName.indexOf("[") + 1, iconName.indexOf("]"));
        }
        if (!iconName.contains("_")) {
            iconName = iconName.replace("e", "emoji_");
        }
        Bitmap emojiBmp = getEmojiIcon(iconName);
        if (emojiBmp == null) {
            //这种情况为用户输入[你好]，符合正则表达式，但是，在里面找不到对应表情的问题；
            return null;
        }
        //为了兼容旧有数据，这里将emoji_001修改为e001
        String oldNameInString = iconName.replace("emoji_", "e");
        SpannableString ss = new SpannableString("[" + oldNameInString + "]");
        Drawable d = new BitmapDrawable(context.getResources(), emojiBmp);
        int emojiSize =
                Util.dip2px(context, 24);
        d.setBounds(0, 0, emojiSize, emojiSize);
        ImageSpan imgSpan = new ImageSpan(d);
        ss.setSpan(imgSpan, 0, oldNameInString.length() + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    /**
     * 将纯文本，转换成带表情的Spannable
     */
    public SpannableStringBuilder convert(Context context, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Pattern p = Pattern.compile("\\[\\S+?\\]");
        Matcher m = p.matcher(builder);
        while (m.find()) {
            int start = m.start();
            String emojiName = m.group();
            // 用表情替换原名字
            SpannableString spannableString = getSpannableByEmojiName(context, emojiName);
            if (spannableString != null) {
                builder.replace(start, start + emojiName.length(),
                        spannableString);
            }
        }
        return builder;
    }
}
