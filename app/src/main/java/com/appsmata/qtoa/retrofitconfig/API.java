package com.appsmata.qtoa.retrofitconfig;

import com.appsmata.qtoa.models.Callback.*;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "User-Agent: QtoAndy";

    String AppChecker = "question2answer.json";
    String AskNow = "qa-api/ask.php";
    String UsersLists = "qa-api/users-lists.php";
    String PostsLists = "qa-api/posts-lists.php";
    String PostsSingle = "qa-api/posts-single.php";
    String PostsAnswers = "qa-api/posts-answers.php";
    String PostsSearch = "qa-api/posts-search.php";
    String PostsSlider = "qa-api/posts-slider.php";
    String PostsByCategory = "qa-api/posts-by-category.php";
    String CategoriesAll = "qa-api/categories.php";
    String Feedback = "qa-api/feedback.php";
    String ShowComment = "qa-api/comment-by-id.php";
    String CountComment = "qa-api/comment-submit.php";
    String BackgroundDrawer = "qa-api/bg-drawer.php";
    String AccessKeyString = "?accesskey=";
    String AccessKeyValue = "AppSmataKey"; // change accesskey with you want, this accesskey must same with your accesskey in admin panel

    @Headers({CACHE, AGENT}) @GET(AppChecker) Call<CallbackApp> AppChecker();

    /*@FormUrlEncoded
    @POST(BaseUrlConfig.UserLastseen)
    Call<CallbackUser> UserLastseen(
            @Field("userid") String userid
    );*/

    @FormUrlEncoded
    @POST(BaseUrlConfig.UserLogin)
    Call<CallbackUser> UserLogin(
            @Field("handle") String handle,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(AskNow)
    Call<CallbackPostSingle> AskNow(
            @Field("userid")  Integer userid,
            @Field("handle")  String handle,
            @Field("email")  String email,
            @Field("categoryid")  String categoryid,
            @Field("title")  String title,
            @Field("content")  String content,
            @Field("tags")  String tags
    );

    @Headers({CACHE, AGENT})
    @GET(CategoriesAll+AccessKeyString+AccessKeyValue)
    Call<CallbackCategory> getCategories();

    @Headers({CACHE, AGENT})
    @GET(PostsLists+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsLists> PostsLists(
            @Query("total") int total,
            @Query("start") int start,
            @Query("sort") String sort
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSingle+AccessKeyString+AccessKeyValue)
    Call<CallbackPostSingle> PostsSingle(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(PostsAnswers+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsLists> PostsAnswers(
            @Query("postid") int postid,
            @Query("total") int total,
            @Query("page") int page
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSearch)
    Call<CallbackPostsSearch> PostsSearch(
            @Query("keyword") String keyword
    );

    @Headers({CACHE, AGENT})
    @GET(PostsSlider+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsSlider> PostsSlider();

    @Headers({CACHE, AGENT})
    @GET(PostsByCategory+AccessKeyString+AccessKeyValue)
    Call<CallbackPostsByCategory> getPostsCategory(
            @Query("category") int categoryid
    );

    @Headers({CACHE, AGENT})
    @GET(CategoriesAll+AccessKeyString+AccessKeyValue)
    Call<CallbackCategory> getCategory();

    @Headers({CACHE, AGENT})
    @GET(ShowComment+AccessKeyString+AccessKeyValue)
    Call<CallbackShowComment> getShowComment(
            @Query("postid") int postid
    );

    @Headers({CACHE, AGENT})
    @GET(CountComment+AccessKeyString+AccessKeyValue)
    Call<CallbackCountComment> getCountComment(
            @Query("postid") long postid
    );

    @Headers({CACHE, AGENT})
    @GET(BackgroundDrawer+AccessKeyString+AccessKeyValue)
    Call<CallbackBackgroundDrawer> getImageDrawer(

    );

    @Headers({CACHE, AGENT})
    @GET(UsersLists+AccessKeyString+AccessKeyValue)
    Call<CallbackUsersLists> UsersLists(
            @Query("total") int total,
            @Query("start") int start,
            @Query("sort") String sort
    );

    @FormUrlEncoded
    @POST(Feedback)
    Call<FeedbackModal> feedBack(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("gender") String gender,
            @Field("city") String city,
            @Field("country") String country,
            @Field("txt_feed") String txt_feed

    );
}
