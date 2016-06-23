/*
 * Copyright (c) 2015, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.dashboard.views.fragments.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.hisp.dhis.android.dashboard.R;
import org.hisp.dhis.android.dashboard.views.fragments.BaseDialogFragment;
import org.hisp.dhis.client.sdk.models.dashboard.Dashboard;
import org.hisp.dhis.client.sdk.ui.views.FontButton;


import static org.hisp.dhis.client.sdk.utils.StringUtils.isEmpty;

/**
 * Handles editing (changing name) and removal of given dashboard.
 */

// TODO Consult if ARG_DASHBOARD_ID implementation in newInstance is correct
public final class DashboardManageFragment extends BaseDialogFragment{
    private static final String TAG = DashboardManageFragment.class.getSimpleName();
    private static final String ARG_DASHBOARD_ID = "arg:dashboardId";

    View mFragmentBar;
    View mFragmentBarEditingMode;
    TextView mDialogLabel;
    TextView mActionName;
    EditText mDashboardName;
    Button mDeleteButton;
    TextInputLayout mTextInputLayout;

    ImageView mCloseDialogButton;
    ImageView mCancelActionButton;
    ImageView mAcceptActionButton;
    FontButton mDeleteDashboardButton;

    Dashboard mDashboard;

    public static DashboardManageFragment newInstance(long dashboardId) {
        Bundle args = new Bundle();
        args.putLong(ARG_DASHBOARD_ID, dashboardId);

        DashboardManageFragment fragment = new DashboardManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE,
                R.style.Theme_AppCompat_Light_Dialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_manage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initViews(view);

        mDialogLabel.setText(getString(R.string.manage_dashboard));
        mActionName.setText(getString(R.string.edit_name));

        mDashboardName.setText(mDashboard.getDisplayName());
        mDeleteButton.setEnabled(mDashboard.getAccess().isDelete());

        setFragmentBarActionMode(false);
    }

    private void initViews(View view){

        mFragmentBar = view.findViewById(R.id.fragment_bar);
        mFragmentBarEditingMode = view.findViewById(R.id.fragment_bar_mode_editing);
        mDialogLabel = (TextView) view.findViewById(R.id.dialog_label);
        mActionName = (TextView) view.findViewById(R.id.action_name);
        mDashboardName = (EditText) view.findViewById(R.id.dashboard_name);
        mDeleteButton = (Button) view.findViewById(R.id.delete_dashboard_button);
        mTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_dashboard_name);

        mCloseDialogButton = (ImageView) view.findViewById(R.id.close_dialog_button);
        mCancelActionButton = (ImageView) view.findViewById(R.id.cancel_action);
        mAcceptActionButton = (ImageView) view.findViewById(R.id.accept_action);
        mDeleteDashboardButton = (FontButton) view.findViewById(R.id.delete_dashboard_button);

        mCloseDialogButton.setOnClickListener(onClickListener);
        mCancelActionButton.setOnClickListener(onClickListener);
        mAcceptActionButton.setOnClickListener(onClickListener);
        mDeleteDashboardButton.setOnClickListener(onClickListener);

        mDashboardName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setFragmentBarActionMode(hasFocus);
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_action: {
                    mDashboardName.setText(
                            mDashboard.getDisplayName());
                    mDashboardName.clearFocus();
                }
                case R.id.accept_action: {
                    boolean isEmptyName = isEmpty(mDashboardName.getText().toString().trim());
                    String message = isEmptyName ? getString(R.string.enter_valid_name) : "";
                    mTextInputLayout.setError(message);

                    if (!isEmptyName) {
                        mDashboard.updateDashboard(mDashboardName.getText().toString());
                        mDashboardName.clearFocus();

                        if (isDhisServiceBound()) {
                            getDhisService().syncDashboards();
                            EventBusProvider.post(new UiEvent(UiEvent.UiEventType.SYNC_DASHBOARDS));
                        }
                        dismiss();
                    }
                    break;
                }
                case R.id.delete_dashboard_button: {
                    mDashboard.deleteDashboard();

                    if (isDhisServiceBound()) {
                        getDhisService().syncDashboards();
                        EventBusProvider.post(new UiEvent(UiEvent.UiEventType.SYNC_DASHBOARDS));
                    }
                }
                case R.id.close_dialog_button: {
                    dismiss();
                }

            }
        }
    };

    /* set fragment bar in editing mode, by hiding standard
    layout and showing layout with actions*/
    void setFragmentBarActionMode(boolean enabled) {
        mFragmentBarEditingMode.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mFragmentBar.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

}
