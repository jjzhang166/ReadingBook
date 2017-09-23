package com.agenthun.readingroutine.views;

import android.content.Context;
import android.util.AttributeSet;


public class BookView extends BaseView {
    private BookPath bookPath;

    public BookView(Context context) {
        super(context);
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BasePath createBasePath() {
        bookPath = new BookPath();
        return bookPath;
    }
}
