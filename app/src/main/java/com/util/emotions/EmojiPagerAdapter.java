package com.util.emotions;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class EmojiPagerAdapter extends PagerAdapter {

    public EmojiPagerAdapter(Context context) {
        mContext = context;
        mEmojiUtils = EmojiUtil.getInstance(context);
    }

    private Context mContext;
    private EmojiUtil mEmojiUtils;
    private static final int EMOJIS_PER_PAGE = 21;
    private static final int EMOJIS_PER_LINE = 7;
    private OnEmojiClickListener mOnEmotionClickListener;

    public void setOnEmotionClickListener(OnEmojiClickListener l) {
        mOnEmotionClickListener = l;
    }

    @Override
    public int getCount() {
        int pages = mEmojiUtils.getEmojiNameArray().length / (EMOJIS_PER_PAGE - 1);
        return mEmojiUtils.getEmojiNameArray().length % (EMOJIS_PER_PAGE - 1) == 0 ? pages : pages + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String[] emojis = getArrayItems(position);
        final EmojiAdapter adapter = new EmojiAdapter(mContext, emojis);

        GridView grid = new GridView(mContext);
        grid.setNumColumns(EMOJIS_PER_LINE);
        grid.setAdapter(adapter);
//        int padding = Util.dip2px(mContext, 8);
//        grid.setHorizontalSpacing(padding);
//        grid.setVerticalSpacing(padding);
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        grid.setCacheColorHint(Color.TRANSPARENT);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setGravity(Gravity.CENTER_HORIZONTAL);
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mOnEmotionClickListener != null) {
                    mOnEmotionClickListener.onEmotionClick(adapter
                            .getItem(position));
                }


            }
        });


        // add to parent
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        container.addView(grid, lp);
        return grid;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private String[] getArrayItems(int position) {

        String[] emojiNameList = mEmojiUtils.getEmojiNameArray();
        String[] array = new String[Math.min(EMOJIS_PER_PAGE, emojiNameList.length - position * (EMOJIS_PER_PAGE - 1))];
        for (int j = 0; j < array.length - 1; j++) {
            array[j] = emojiNameList[(position * (EMOJIS_PER_PAGE - 1) + j)];
        }
        array[array.length - 1] = EmojiUtil.BACKSPACE;
        return array;
    }
}
