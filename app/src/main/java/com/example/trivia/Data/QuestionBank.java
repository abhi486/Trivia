package com.example.trivia.Data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trivia.Controller.AppController;
import com.example.trivia.Model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.trivia.Controller.AppController.TAG;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerQuestionAsync Checkprocess){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++)
                        {
                            Question question = new Question();
                            try {
                                //Add elements to questionarraylist
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswertrue(response.getJSONArray(i).getBoolean(1));
                                questionArrayList.add(question);
                                //Log.d(TAG, "onResponse: " + response.getJSONArray(i).get(0));
                                //Log.d(TAG, "onResponse: " + response.getJSONArray(i).getBoolean(1));
                                //Log.d(TAG, "onResponse: " + question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (Checkprocess!=null)
                            Checkprocess.Processfinished(questionArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
