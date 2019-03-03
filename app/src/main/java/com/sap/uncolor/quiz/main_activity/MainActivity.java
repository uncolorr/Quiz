package com.sap.uncolor.quiz.main_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sap.uncolor.quiz.AuthActivity;
import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.RoomViewRenderer;
import com.sap.uncolor.quiz.TopActivity;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.create_private_table_activity.CreatePrivateTableActivity;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.dialogs.EditAvatarDialog;
import com.sap.uncolor.quiz.dialogs.MessageReporter;
import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetUserByIdRequestData;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.PathConverter;
import com.sap.uncolor.quiz.utils.RawTextReader;
import com.sap.uncolor.quiz.utils.TextFormatter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener {

    private static final int REQUEST_FOR_AVATAR_UPLOAD_FROM_GALLERY = 1;

    @BindView(R.id.buttonSingleGame)
    Button buttonSingleGame;

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.textViewPoints)
    TextView textViewPoints;

    @BindView(R.id.recyclerViewCurrentGames)
    RecyclerView recyclerViewCurrentGames;

    @BindView(R.id.imageViewAvatar)
    CircleImageView imageViewAvatar;

    @BindView(R.id.imageViewWarning)
    ImageView imageViewWarning;

    @BindView(R.id.textViewCurrentRoomMessage)
    TextView textViewCurrentRoomMessage;

    @BindView(R.id.textViewWinsAndLoses)
    TextView textViewWinsAndLoses;

    @BindView(R.id.buttonReloadCurrentRooms)
    Button buttonReloadCurrentRooms;

    @BindView(R.id.progressBarCurrentRoomsLoading)
    ProgressBar progressBarCurrentRoomsLoading;

    @BindView(R.id.adView)
    AdView adView;


    private AlertDialog loadingDialog;

    private UniversalAdapter adapter;

    private EditAvatarDialog editAvatarDialog;


    public static Intent getInstance(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        editAvatarDialog = new EditAvatarDialog(this);
        editAvatarDialog.setOnUploadAvatarClickListener(getOnUploadAvatarClickListener());
        editAvatarDialog.setOnRemoveClickListener(getOnRemoveAvatarClickListener());
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        textViewName.setText(App.getUserName());
        textViewPoints.setText(Integer.toString(App.getUserPoints()));
        textViewWinsAndLoses.setText(TextFormatter.toNormalWinsLosesFormat(
                App.getUser().getWinsCount(),
                App.getUser().getLosesCount()));
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new RoomViewRenderer(Room.TYPE, this));
        recyclerViewCurrentGames.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCurrentGames.setAdapter(adapter);
    }

    @OnClick(R.id.buttonSingleGame)
    void onButtonSingleGameClick() {
        DBManager dbManager = new DBManager(this);
        if (dbManager.getCompletedRoundsCount() >= 5) {
            dbManager.clearSingleGameResults();
            dbManager.close();
        }
        showLoadingDialog();
        Api.getSource().getQuestions(new GetQuestionsRequestData())
                .enqueue(ApiResponse.getCallback(getQuestionsResponseListener(), this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCurrentRoomsProgressBar();
        adapter.clear();
        Api.getSource().getRooms(new GetRoomsRequestData())
                .enqueue(ApiResponse.getCallback(getRoomsResponseListener(), getRoomsFailureListener()));
        Api.getSource().getUserById(new GetUserByIdRequestData())
                .enqueue(ApiResponse.getCallback(getUserByIdResponseListener(), getUserByIdFailureListener()));
    }

    private ApiResponse.ApiFailureListener getUserByIdFailureListener() {
        return new ApiResponse.ApiFailureListener() {
            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(MainActivity.this,
                        "Не удалось обновить информацию о пользователе",
                        Toast.LENGTH_LONG).show();
            }
        };
    }

    private ApiResponse.ApiFailureListener getRoomsFailureListener() {
        return new ApiResponse.ApiFailureListener() {
            @Override
            public void onFailure(int code, String message) {
                hideCurrentRoomsProgressBar();
                showInfoAboutCurrentRoomsLoadingFailure();
            }
        };
    }

    private ApiResponse.ApiResponseListener<ResponseModel<User>> getUserByIdResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<User>>() {
            @Override
            public void onResponse(ResponseModel<User> result) {
                if (result == null || result.getResult() == null) {
                    Toast.makeText(MainActivity.this,
                            "Не удалось обновить информацию о пользователе",
                            Toast.LENGTH_LONG).show();
                } else {
                    User user = result.getResult();
                    showUserInfo(user);
                    App.updateUserData(user);
                }
            }
        };
    }


    @SuppressLint("SetTextI18n")
    private void showUserInfo(User user) {
        textViewName.setText(user.getLogin());
        textViewPoints.setText(Integer.toString(user.getPoints()));
        textViewWinsAndLoses.setText(TextFormatter.toNormalWinsLosesFormat(
                App.getUser().getWinsCount(),
                App.getUser().getLosesCount()));
        if (user.getAvatar().isEmpty()) {
            if (user.getSex().equals(User.SEX_TYPE_MALE)) {
                imageViewAvatar.setImageResource(R.drawable.boy);
            } else if (user.getSex().equals(User.SEX_TYPE_FEMALE)) {
                imageViewAvatar.setImageResource(R.drawable.girl);
            }
        } else {
            Glide
                    .with(getApplicationContext())
                    .load(user.getAvatar())
                    .into(imageViewAvatar);
            editAvatarDialog.uploadAvatar(user.getAvatar());
        }
    }

    private View.OnClickListener getOnUploadAvatarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_FOR_AVATAR_UPLOAD_FROM_GALLERY);
            }
        };
    }

    private View.OnClickListener getOnRemoveAvatarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAvatar(null);
            }
        };
    }


    private ApiResponse.ApiResponseListener<ResponseModel<List<Room>>> getRoomsResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Room>>>() {
            @Override
            public void onResponse(ResponseModel<List<Room>> result) {
                hideCurrentRoomsProgressBar();
                if (result == null || result.getResult() == null) {
                    showInfoAboutCurrentRoomsLoadingFailure();

                } else {
                    adapter.clear();
                    List<Room> rooms = result.getResult();
                    if (rooms.isEmpty()) {
                        showInfoAboutEmptyCurrentRooms();
                        return;
                    }
                    hideInfoAboutEmptyCurrentRooms();
                    hideInfoAboutCurrentRoomsLoadingFailure();
                    for (int i = 0; i < rooms.size(); i++) {
                        adapter.add(rooms.get(i));
                    }
                }
            }
        };
    }

    @OnClick(R.id.imageViewAvatar)
    void onImageAvatarClick() {
        editAvatarDialog.show();
    }

    @OnClick(R.id.buttonMenuInfo)
    void onButtonMenuInfoClick(){
        MessageReporter.showMessageWithInfo(this, "Информация о меню игры",
                RawTextReader.readRawTextFile(this, R.raw.game_menu_info));
    }

    @OnClick(R.id.buttonCurrentGamesInfo)
    void onButtonCurrentGamesInfoClick(){
        MessageReporter.showMessageWithInfo(this, "Информация о текущих играх",
                RawTextReader.readRawTextFile(this, R.raw.current_games_info));
    }

    @OnClick(R.id.imageButtonTop)
    void onTopButtonClick() {
        startActivity(TopActivity.getInstance(this));
    }

    @OnClick(R.id.imageButtonExit)
    void onExitButtonClick() {
        MessageReporter.showConfirmForLogout(this, getLogoutClickListener());
    }

    private DialogInterface.OnClickListener getLogoutClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.logout();
                finish();
                startActivity(AuthActivity.getInstance(MainActivity.this));
            }
        };
    }


    @OnClick(R.id.buttonPrivateGame)
    void onButtonPrivateGameClick() {
        startActivity(CreatePrivateTableActivity.getInstance(this));
    }

    @OnClick(R.id.buttonOnlineGame)
    void onButtonOnlineGameClick() {
        showLoadingDialog();
        Api.getSource().getRoom(new GetRoomRequestData())
                .enqueue(ApiResponse.getCallback(getFindRoomResponseListener(), this));
    }

    @OnClick(R.id.buttonReloadCurrentRooms)
    void onReloadCurrentRoomsButtonClick() {
        showCurrentRoomsProgressBar();
        hideInfoAboutCurrentRoomsLoadingFailure();
        hideInfoAboutEmptyCurrentRooms();
        Api.getSource().getRooms(new GetRoomsRequestData())
                .enqueue(ApiResponse.getCallback(getRoomsResponseListener(), getRoomsFailureListener()));
    }


    private void showInfoAboutEmptyCurrentRooms() {
        imageViewWarning.setVisibility(View.INVISIBLE);
        buttonReloadCurrentRooms.setVisibility(View.VISIBLE);
        textViewCurrentRoomMessage.setVisibility(View.VISIBLE);
        textViewCurrentRoomMessage.setText("Текущих игр нет");
    }

    private void showInfoAboutCurrentRoomsLoadingFailure() {
        imageViewWarning.setVisibility(View.VISIBLE);
        buttonReloadCurrentRooms.setVisibility(View.VISIBLE);
        textViewCurrentRoomMessage.setVisibility(View.VISIBLE);
        textViewCurrentRoomMessage.setText("Ошибка при загрузке текущих поединков");
    }

    private void hideInfoAboutEmptyCurrentRooms() {
        textViewCurrentRoomMessage.setVisibility(View.INVISIBLE);
    }

    private void hideInfoAboutCurrentRoomsLoadingFailure() {
        imageViewWarning.setVisibility(View.INVISIBLE);
        textViewCurrentRoomMessage.setVisibility(View.INVISIBLE);
        buttonReloadCurrentRooms.setVisibility(View.INVISIBLE);
    }

    private void showCurrentRoomsProgressBar() {
        progressBarCurrentRoomsLoading.setVisibility(View.VISIBLE);
    }

    private void hideCurrentRoomsProgressBar() {
        progressBarCurrentRoomsLoading.setVisibility(View.INVISIBLE);
    }

    private ApiResponse.ApiResponseListener<ResponseModel<Room>> getFindRoomResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<Room>>() {
            @Override
            public void onResponse(ResponseModel<Room> result) {
                cancelLoadingDialog();
                if (result == null || result.getResult() == null) {
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка при поиске игры");
                } else {
                    Room room = result.getResult();
                    startActivity(ResultsActivity
                            .getInstanceForOnlineGame(MainActivity.this, room));
                }
            }
        };
    }


    private ApiResponse.ApiResponseListener<ResponseModel<List<Question>>> getQuestionsResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Question>>>() {
            @Override
            public void onResponse(ResponseModel<List<Question>> result) {
                cancelLoadingDialog();
                if (result == null || result.getResult() == null) {
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка создания игры");
                } else {
                    Quiz quiz = new Quiz();
                    quiz.setQuestions(result.getResult());
                    startActivity(QuizActivity.getInstanceForSingleGame(MainActivity.this, quiz));
                }
            }
        };
    }

    public void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void cancelLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    @Override
    public void onFailure(int code, String message) {
        cancelLoadingDialog();
        MessageReporter.showMessage(MainActivity.this,
                "Ошибка",
                "Неизвестная ошибка");
    }

    private void uploadAvatar(File file) {
        User user = App.getUser();
        String tokenString = user.getToken();
        RequestBody token = RequestBody.create(MultipartBody.FORM, tokenString);
        if(file == null){
            Api.getSource().removeAvatar(token).
                    enqueue(ApiResponse.getCallback(getUploadAvatarResponseListener(), this));
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        Api.getSource().changeAvatar(token, body).
                enqueue(ApiResponse.getCallback(getUploadAvatarResponseListener(), this));

    }

    private ApiResponse.ApiResponseListener<ResponseModel<String>> getUploadAvatarResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<String>>() {
            @Override
            public void onResponse(ResponseModel<String> result) {
                App.Log("response on upload avatar");
                if (result == null || result.getResult() == null) {
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка обновления аватара");
                } else {
                    String avatarUrl = result.getResult();
                    if (avatarUrl.isEmpty()) {
                        if (App.getUser().getSex().equals(User.SEX_TYPE_MALE)) {
                            imageViewAvatar.setImageResource(R.drawable.boy);
                        } else if (App.getUser().getSex().equals(User.SEX_TYPE_FEMALE)) {
                            imageViewAvatar.setImageResource(R.drawable.girl);
                        }
                        Toast.makeText(MainActivity.this,
                                "Аватар успешно удален", Toast.LENGTH_LONG).show();
                        editAvatarDialog.uploadAvatar("");
                        return;
                    }
                    Glide
                            .with(getApplicationContext())
                            .load(avatarUrl)
                            .into(imageViewAvatar);
                    editAvatarDialog.uploadAvatar(avatarUrl);
                    User user = App.getUser();
                    user.setAvatar(avatarUrl);
                    App.updateUserData(user);
                    Toast.makeText(MainActivity.this,
                            "Аватар успешно обновлен", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FOR_AVATAR_UPLOAD_FROM_GALLERY:
                if (data != null) {
                    Uri avatarUri = data.getData();
                    String path = PathConverter.getRealPathFromURI(this, avatarUri);
                    App.Log(path);
                    File file = new File(path);
                    uploadAvatar(file);
                }
                break;
        }
    }
}
