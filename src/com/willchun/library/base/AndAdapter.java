/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * AndAdapter.java
 *
 */
package com.willchun.library.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 *@author willchun (wcly10@gmail.com)
 *@date 2013-5-21
 */
public abstract class AndAdapter<T> extends BaseAdapter {
    private final int resId;
    protected Activity activity;
	protected final AndQuery aq;
    private final Class<? extends IViewHolder<T>> itemClass;

    public AndAdapter(int resId, Activity activity, Class<? extends IViewHolder<T>> itemClass) {
        this.resId = resId;
        this.activity = activity;
        this.itemClass = itemClass;
		this.aq = new AndQuery(activity);
    }

    @Override
    public abstract T getItem(int position);

    protected View createItemView() {
        return View.inflate(activity, resId, null);
    }

    protected IViewHolder<T> createViewHolder() {
        try {
            return itemClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(itemClass.getName() + " 初始化失败！请确认类"
                    + itemClass.getSimpleName() + "含有一个无参的构造函数");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(itemClass.getName() + " 初始化失败！请确认类"
                    + itemClass.getSimpleName()
                    + "是一个public static class");
        } catch (NullPointerException e) {
            throw new RuntimeException(" 初始化失败！itemClass 不能为空");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IViewHolder<T> holder = null;
        if (convertView == null) {
            convertView = createItemView();
            holder = createViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (IViewHolder<T>) convertView.getTag();
        }
        holder.update(getItem(position), aq.recycle(convertView), this, position, parent);
        return convertView;
    }

    public static interface IViewHolder<T> {
		void update(T item, AndQuery aq, AndAdapter<T> adapter, int position,
				ViewGroup parent);
    }

    public abstract static class ViewHolder<T> implements IViewHolder<T> {

        @Override
		public void update(T item, AndQuery aq, AndAdapter<T> adapter,
				int position,
                ViewGroup parent) {
            update(item, aq);
        }

		public abstract void update(T item, AndQuery aq);
    }

}
