package com.example.geetion.rxnews.RXNews;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geetion.rxnews.ArticleWebview;
import com.example.geetion.rxnews.Model.NormalRXNewsArticle;
import com.example.geetion.rxnews.Model.SlideRXNewsArticle;
import com.example.geetion.rxnews.NetWorkRequest.NetWorkUtils;
import com.example.geetion.rxnews.R;
import com.example.geetion.rxnews.RecyclerViewOnClickInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RXNewsFragment extends Fragment implements RecyclerViewOnClickInterface{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mrecyclerView;

    private final String murl = "http://app.ecjtu.net/api/v1/index";
    private final String marticleUrl = "http://app.ecjtu.net/api/v1/article/";

    private ArrayList<NormalRXNewsArticle> normalArticles = new ArrayList<>();
    private ArrayList<SlideRXNewsArticle> slideArticles = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisView = inflater.inflate(R.layout.fragment_rxnews, container, false);
        initLayout(thisView);
        getHttpRequestData();

        return thisView;
    }

    private void initLayout(View view){

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.rxNewsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpRequestData();
            }
        });

        mrecyclerView = (RecyclerView)view.findViewById(R.id.rxNewsRecyclerView);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //获取网络数据
    private void getHttpRequestData(){
        NetWorkUtils.getHttpRequest(murl, new NetWorkUtils.OnRequestSuccess() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //处理一般文章数据
                    JSONObject normal_articleJSONArray = jsonObject.getJSONObject("normal_article");
                    ArrayList<NormalRXNewsArticle> tmpNormalArticles = new ArrayList<>();
                    parseNorlmalArticleJSONObject(normal_articleJSONArray,tmpNormalArticles);
                    normalArticles = tmpNormalArticles;
                    //处理横幅文章数据
                    JSONObject slide_articleJSONArray = jsonObject.getJSONObject("slide_article");
                    ArrayList<SlideRXNewsArticle> tmpSlideArticles = new ArrayList<>();
                    parseSlideArticleJSONObject(slide_articleJSONArray,tmpSlideArticles);
                    slideArticles = tmpSlideArticles;

                    mrecyclerView.setAdapter(new RXNewsRecyclerViewAdapter(normalArticles,RXNewsFragment.this));
                    swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetWorkUtils.OnRequestfail() {
            @Override
            public void onFail(String e) {

            }
        });
    }

    /**
     * 解析一般文章数据
     * @param jsonObject
     */
    private void parseNorlmalArticleJSONObject(JSONObject jsonObject,ArrayList<NormalRXNewsArticle> articles) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                NormalRXNewsArticle article = new NormalRXNewsArticle(object);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析横幅文章数据
     * @param jsonObject
     */
    private void parseSlideArticleJSONObject(JSONObject jsonObject,ArrayList<SlideRXNewsArticle> articles){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SlideRXNewsArticle article = new SlideRXNewsArticle(object);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        NormalRXNewsArticle article = normalArticles.get(position);
        String url = "http://app.ecjtu.net/api/v1/article/"+String.valueOf(article.id)+"/view";
        Intent intent = new Intent(getActivity(), ArticleWebview.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
