package com.nerus.apparquos.adapters;

public interface OnItemListener{
    void onItemClick(Integer position, Object item);
    void onItemValueChanged(Object itemChanged);
    void onItemRemoved(Object itemToRemove);
}