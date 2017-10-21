package com.uservoice.uservoicesdk.model;

import com.uservoice.uservoicesdk.babayaga.Babayaga;
import com.uservoice.uservoicesdk.rest.Callback;
import com.uservoice.uservoicesdk.rest.RestTaskCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Ticket extends BaseModel {

    public static void createTicket(String message, Map<String, String> customFields, final Callback<Ticket> callback) {
        createTicket(message, null, null, null, customFields, callback);
    }

    public static void createTicket(String message, String subject, String email, String name, Map<String, String> customFields, final Callback<Ticket> callback) {
        Map<String, String> params = new HashMap<String, String>();

        if(subject != null) {
            params.put("ticket[subject]", message);
        }

        params.put("ticket[message]", message);

        if (email != null)
            params.put("email", email);

        if (name != null)
            params.put("display_name", name);

        if (Babayaga.getUvts() != null)
            params.put("uvts", Babayaga.getUvts());

        for (Map.Entry<String, String> entry : getSession().getExternalIds().entrySet()) {
            params.put(String.format("created_by[external_ids][%s]", entry.getKey()), entry.getValue());
        }

        if (getConfig().getCustomFields() != null) {
            for (Map.Entry<String, String> entry : getConfig().getCustomFields().entrySet()) {
                params.put(String.format("ticket[custom_field_values][%s]", entry.getKey()), entry.getValue());
            }
        }

        if (customFields != null) {
            for (Map.Entry<String, String> entry : customFields.entrySet()) {
                params.put(String.format("ticket[custom_field_values][%s]", entry.getKey()), entry.getValue());
            }
        }

        doPost(apiPath("/tickets.json"), params, new RestTaskCallback(callback) {
            @Override
            public void onComplete(JSONObject result) throws JSONException {
                callback.onModel(deserializeObject(result, "ticket", Ticket.class));
            }
        });
    }

}