/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * ModelAdapter.java
 *
 */
package com.willchun.library.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.willchun.library.base.AndAdapter;

import android.app.Activity;


/**
 * @author willchun (wcly10@gmail.com)
 * @date 2013-5-21
 */
public class ModelAdapter<T> extends AndAdapter<T> {
    protected List<T> objects;

    public ModelAdapter(Activity activity, List<T> objects, int resId, Class<? extends IViewHolder<T>> itemClass) {
        super(resId, activity, itemClass);
        if (objects == null) {
            this.objects = new ArrayList<T>();
        } else {
            this.objects = objects;
        }
    }

    public ModelAdapter(Activity activity, int resId, Class<? extends IViewHolder<T>> itemClass) {
        this(activity, new ArrayList<T>(), resId, itemClass);
    }

    public ModelAdapter(Activity activity, List<T> objects, int resId) {
        this(activity, objects, resId, null);
    }

    public ModelAdapter(Activity activity, int resId) {
        this(activity, new ArrayList<T>(), resId, null);
    }

    public ModelAdapter(Activity activity, T[] objects, int resId, Class<? extends IViewHolder<T>> itemClass) {
        this(activity, Arrays.asList(objects), resId, itemClass);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(T object) {
        if (object != null) {
            objects.add(object);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> items) {
        if (items != null) {
            objects.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addItems(T... items) {
        addItems(Arrays.asList(items));
    }

    public void setItems(List<T> items) {
        objects.clear();
        addItems(items);
    }

    public void setItems(T... items) {
        objects.clear();
        addItems(items);
    }

    public List<T> getItems() {
        return objects;
    }

    public void deleteItem(int position) {
        objects.remove(position);
        notifyDataSetChanged();
    }

    public void deleteItems(List<T> items) {
        objects.removeAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        if (objects != null) {
            objects.clear();
        }
        notifyDataSetChanged();
    }
}
