package xuwei.com.passwordgenerator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xuwei.com.passwordgenerator.R;

/**
 * Created by xuweichen on 2017/10/20.
 */

public class OtherFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.other_fragment, container, false);
    }
}
