package xuwei.com.passwordgenerator.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import xuwei.com.passwordgenerator.R;

/**
 * Created by xuweichen on 2017/10/20.
 */

public class BankcardFragment extends Fragment {
    private static final String TAG = "BankcardFragment";

    private static int MY_SCAN_REQUEST_CODE = 32423;
    private EditText bankCardId;
    private View bankCardScan;
    private View passwordCreate;
    private TextView password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bankcard_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        bankCardId = (EditText) rootView.findViewById(R.id.bank_card_id);
        bankCardScan = rootView.findViewById(R.id.bank_card_scan);
        passwordCreate = rootView.findViewById(R.id.password_create);
        password = (TextView) rootView.findViewById(R.id.password);

        bankCardScan.setOnClickListener(myClickListener);
        passwordCreate.setOnClickListener(myClickListener);
    }

    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.bank_card_scan:
                    scanCard();
                    break;
                case R.id.password_create:
                    passwordCreate();
                    break;
                default:
                    break;
            }
        }
    };

    private void scanCard() {
        Intent scanIntent = new Intent(this.getContext(), CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                bankCardId.setText(scanResult.getRedactedCardNumber());
            }
        }
    }

    private void passwordCreate() {
        String cardNo = bankCardId.getText().toString().trim();
        if (TextUtils.isEmpty(cardNo)) {
            Toast.makeText(this.getContext(), "请输入银行卡卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkBankCard(cardNo)) {
            Toast.makeText(this.getContext(), "请检查银行卡卡号是否正确", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordStr = "XX" + computeHash(cardNo);
        password.setText(passwordStr);
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    private boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    private char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    private String computeHash(String cardNo) {
        int hash = cardNo.hashCode();
        while (hash <= 1000) {
            hash *= hash;
        }
        hash = hash % 10000;
        return String.format("%04d", hash);
    }
}
