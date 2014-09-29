package com.willchun.library.adapter;/**
 * Created by Administrator on 2014/9/23.
 */

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import java.util.*;

/**
 * 混合型的选择适配器，支持单选，多选，混合选择
 * 混合模式下：当是关键的position被选中的时候，会排斥其他不再key表中的数据
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/9/23
 */
public abstract class MixedChoiceAdapter<T> extends ArrayAdapter<T> implements AdapterView.OnItemClickListener {

    public static enum MODE {
        SINGLE, MULTI, MIXED
    }

    private MODE mode =MODE.MIXED;;
    /**
     * 数据源
     */
    private List<T> mItems;

    /**
     * 布局资源id
     */
    private int resourceId;

    private Set<Integer> mSelectedSets = new HashSet<Integer>();//被选择的item的存储器

    private Set<Integer> mMixedKeySets = new HashSet<Integer>();//混淆模式下的关键key值组

    private int mBgDefaultId;
    private int mBgSelectedId;

    private int MAX_NUMBE = 0;//最大选择数 0代表无限大

    private Activity activity;

    private OnMixedChoiceListener mMixedChooseListener = null;

    private int maxNumber;//最大选择数

    public MixedChoiceAdapter(Activity activity, int resource, List<T> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.resourceId = resource;
        this.mItems = objects;
    }

    public MixedChoiceAdapter(Activity activity, int resource) {
        this(activity, resource, new ArrayList<T>());
    }

    public List<T> getmItems() {
        return this.mItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(activity, resourceId, null);
        }
        updateBgResource(convertView, position);

        update(getItem(position), convertView, position);
        return convertView;
    }

    protected abstract void update(T item, View root, int position);


    @Override
    public void addAll(Collection<? extends T> collection) {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            for (T t : collection) {
                add(t);
            }
        } else {
            super.addAll(collection);
        }
    }

    @Override
    public void addAll(T... items) {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            for (T t : items) {
                add(t);
            }
        } else {
            super.addAll(items);
        }
    }

    /**
     * 设置Item默认和选择的效果
     *
     * @param defaultBgId
     * @param selectedBgId
     */
    public void setItemBg(int defaultBgId, int selectedBgId) {
        this.mBgDefaultId = defaultBgId;
        this.mBgSelectedId = selectedBgId;
        notifyDataSetChanged();
    }

    public void setMaxNumber(int maxNumber){
        this.maxNumber = maxNumber;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (mode){
            case SINGLE:
                break;
            case MULTI:
                if(!mSelectedSets.contains(position)){
                    if(maxNumber != 0 && mSelectedSets.size() >= maxNumber){
                        if(mMixedChooseListener != null){
                            mMixedChooseListener.onCurrentClickItem(position, mSelectedSets.contains(position));
                        }
                        return;
                    }
                }
                break;
            case MIXED:
                if(!mSelectedSets.contains(position) && !mMixedKeySets.contains(position)) {
                    List<Integer> noKeyList = new ArrayList<Integer>();
                    List<Integer> cangkuList = getCurrentSelectedPosition();
                    for (int tmp : cangkuList) {
                        if (!mMixedKeySets.contains(tmp)) {
                            noKeyList.add(tmp);
                        }
                    }
                    if (maxNumber != 0 && noKeyList.size() >= maxNumber) {
                        if (mMixedChooseListener != null) {
                            mMixedChooseListener.onCurrentClickItem(position, mSelectedSets.contains(position));
                        }
                        return;
                    }
                }
                break;
        }
        //针对点击的item， 获取前一刻的点击状态并改变为当前的点击状态
        if(mSelectedSets.contains(position)){
            mSelectedSets.remove(position);
        }else{
            mSelectedSets.add(position);
        }

        modeDeal(position, mSelectedSets.contains(position));

        if(mMixedChooseListener != null){
            mMixedChooseListener.onCurrentClickItem(position, mSelectedSets.contains(position));
        }

        notifyDataSetChanged();
    }



    /**
     * 更新item的背景信息
     * @param view
     * @param position
     */
    private void updateBgResource(View view, int position){
        if(mBgDefaultId == 0 || mBgSelectedId == 0){
            return;
        }
        if(mSelectedSets.contains(position)){
            view.setBackgroundResource(mBgSelectedId);
        }else{
            view.setBackgroundResource(mBgDefaultId);
        }
    }

    public interface OnMixedChoiceListener {
        /**
         * 当前被点击的item的状态信息
         * @param position
         * @param status
         */
        void onCurrentClickItem(int position, boolean status);

    }

    public void setOnMixedChoiceListener(OnMixedChoiceListener listener){
        this.mMixedChooseListener = listener;
    }

    public List<Integer> getCurrentSelectedPosition(){
        List<Integer> ret = new ArrayList<Integer>();
        Iterator<Integer> iterator = mSelectedSets.iterator();
        while(iterator.hasNext()){
            ret.add(iterator.next());
        }
        return ret;
    }

    public void setMode(MODE mode){
        this.mode = mode;
        notifyDataSetChanged();
    }

    /**
     * 核心的各种模式下数据的处理
     */
    private void modeDeal(int position, boolean status){
        //当取消当前点击效果的时候 是不需要对现有的数据进行处理的
        switch (mode){
            case SINGLE:
                mSelectedSets.clear();
                if(status)
                    mSelectedSets.add(position);
                break;
            case MULTI:
                //多选择本来就是天生属性

                break;
            case MIXED:
                //当一个选择来自key 那么清除掉互斥相
                if(mMixedKeySets.contains(position)){
                    List<Integer> saveList  = new ArrayList<Integer>();
                    List<Integer> cangkuList = getCurrentSelectedPosition();
                    for(int tmp : cangkuList){
                        if(mMixedKeySets.contains(tmp)){
                            saveList.add(tmp);
                        }
                    }
                    if(saveList.size() > 0){
                        mSelectedSets.clear();
                        mSelectedSets.addAll(saveList);
                    }

                }else{
                    mSelectedSets.removeAll(mMixedKeySets);
                }
                break;
        }
    }

    /**
     * 混淆模式下 设置排它的关键keys组
     * @param keys
     */
    public void setMixedKeys(Integer... keys){
        mMixedKeySets.clear();
        mMixedKeySets.addAll(Arrays.asList(keys));
    }

    public List<Integer> getMixedKeys(){
        List<Integer> ret = new ArrayList<Integer>();
        Iterator<Integer> iterator = mMixedKeySets.iterator();
        while(iterator.hasNext()){
            ret.add(iterator.next());
        }
        return ret;
    }
}
