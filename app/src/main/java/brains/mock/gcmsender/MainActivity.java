package brains.mock.gcmsender;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import brains.mock.gcmsender.api.ApiManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

public class MainActivity extends BaseActivity {

    @BindView(R.id.messageField)
    EditText vMessageField;
    @BindView(R.id.progressBar)
    ProgressBar vProgressBar;
    @BindView(R.id.resultText)
    TextView vResultText;

    private ApiManager mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mApiManager = new ApiManager(this);
    }

    @OnClick(R.id.buttonSend)
    void sendMessage() {
        String message = vMessageField.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Log.e(TAG, "Message field is empty");
            vResultText.setText(R.string.enterMessage);
        } else if (Const.API_KEY.length() < 25) {
            Log.e(TAG, "ApiKey error");
            vResultText.setText(getString(R.string.errorMessage, getString(R.string.errorApiKey)));
        } else {
            Subscription sendMessageSubscription = mApiManager.sendPushNotification(Const.API_KEY, vMessageField.getText().toString(), Const.TO_PARAM)
                    .doOnSubscribe(() -> {
                        vProgressBar.setVisibility(View.VISIBLE);
                        vResultText.setText("");
                    }).subscribe(
                            result -> vResultText.setText(getString(R.string.messageId, result.getMessageId())),
                            e -> {
                                Log.e(TAG, "onError: ", e);
                                String errorMessage;
                                if (e instanceof HttpException) {
                                    if (((HttpException) e).code() == 401) {
                                        errorMessage = getString(R.string.errorMessage, getString(R.string.errorApiKey));
                                    } else {
                                        errorMessage = getString(R.string.errorMessage, e.getMessage());
                                    }
                                } else {
                                    errorMessage = getString(R.string.errorMessage, e.getMessage());
                                }
                                vResultText.setText(errorMessage);
                                vProgressBar.setVisibility(View.GONE);
                            },
                            () -> vProgressBar.setVisibility(View.GONE)
                    );
            addSubscription(sendMessageSubscription);
        }
    }
}
