package org.smartregister.chw.vmmc_sample.activity;

import static org.smartregister.chw.vmmc.util.Constants.ENCOUNTER_TYPE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.vmmc.activity.BaseVmmcVisitActivity;
import org.smartregister.chw.vmmc.domain.MemberObject;
import org.smartregister.chw.vmmc.presenter.BaseVmmcVisitPresenter;
import org.smartregister.chw.vmmc.util.Constants;
import org.smartregister.chw.vmmc_sample.R;
import org.smartregister.chw.vmmc_sample.interactor.VmmcVisitDischargeInteractor;
import org.smartregister.util.LangUtils;

import timber.log.Timber;


public class VmmcDischargeActivity extends BaseVmmcVisitActivity {

    public static void startVmmcVisitDischargeActivity(Activity activity, String baseEntityId, Boolean editMode) {
        Intent intent = new Intent(activity, VmmcDischargeActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, editMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, Constants.PROFILE_TYPES.VMMC_PROFILE);
        activity.startActivity(intent);
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return EntryActivity.getSampleMember();
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseVmmcVisitPresenter(memberObject, this, new VmmcVisitDischargeInteractor(Constants.EVENT_TYPE.VMMC_DISCHARGE));
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, SampleJsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void submittedAndClose() {
        Intent returnIntent = new Intent();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(actionList.get(getString(R.string.vmmc_post_discharge)).getJsonPayload());
            jsonObject.put(ENCOUNTER_TYPE, Constants.EVENT_TYPE.VMMC_DISCHARGE);
        } catch (JSONException e) {
            Timber.e(e);
        }
        returnIntent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonObject.toString());
        setResult(Activity.RESULT_OK, returnIntent);
        close();
    }

    @Override
    protected void attachBaseContext(Context base) {
        // get language from prefs
        String lang = LangUtils.getLanguage(base.getApplicationContext());
        super.attachBaseContext(LangUtils.setAppLocale(base, lang));
    }
}
