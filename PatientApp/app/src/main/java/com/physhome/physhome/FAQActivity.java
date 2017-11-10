package com.physhome.physhome;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class FAQActivity extends BaseActivity {
    RecyclerView recyclerView;
    FAQRecyclerViewAdapter adapter;
    ArrayList<FAQModel> faqModels;
    String q_text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat?";
    String a_text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        recyclerView = (RecyclerView) findViewById(R.id.faq_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void prepareListData() {
        faqModels = new ArrayList<>();
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
        faqModels.add(new FAQModel(q_text,a_text));
    }
    void updateRecyclerView(ArrayList<FAQModel> list){
        adapter = new FAQRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // data set changed
    }
    @Override
    protected void onResume() {
        super.onResume();
        prepareListData();
        updateRecyclerView(faqModels);
    }
}
