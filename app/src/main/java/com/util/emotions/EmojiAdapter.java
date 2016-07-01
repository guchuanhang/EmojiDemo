package com.util.emotions;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EmojiAdapter extends BaseAdapter {

    public EmojiAdapter(Context context, String[] emojis) {
        mEmojiArray = emojis;
        mEmojiUtils = EmojiUtil.getInstance(context);
        this.mContext = context;
    }

    private String[] mEmojiArray;
    private EmojiUtil mEmojiUtils;
    private Context mContext;

    @Override
    public int getCount() {
        return mEmojiArray == null ? 0 : mEmojiArray.length;
    }

    @Override
    public String getItem(int position) {
        return mEmojiArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LinearLayout totalLayout = new LinearLayout(mContext);
            totalLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(mContext);

            imageView.setPadding(Util.dip2px(mContext, 8), Util.dip2px(mContext, 8), Util.dip2px(mContext, 8), Util.dip2px(mContext, 8));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            totalLayout.addView(imageView);
            LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            imageParams.height = Util.dip2px(mContext, 48);
            imageParams.width = Util.dip2px(mContext, 48);

            convertView = totalLayout;
            viewHolder.iconView = imageView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String emojiName = getItem(position);
        if (!TextUtils.isEmpty(emojiName)) {
            (viewHolder.iconView).setImageBitmap(mEmojiUtils
                    .getEmojiIcon(emojiName));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iconView;
    }
}
