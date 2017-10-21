package br.com.mobiclub.quantoprima.ui.helper;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.domain.MobiClubUser;
import br.com.mobiclub.quantoprima.facade.Facade;
import br.com.mobiclub.quantoprima.util.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 *
 */
public class SignupHelper
        implements AdapterView.OnItemSelectedListener {

    public static final String INVALID_BIRTH_DATE = "Data de nascimento inválida. Correto dd/mm/aaaa.";
    public static final String GENDER_INVALID = "Sexo não pode ser vazio";
    public static final String PASSWORD_DOENST_MATCH = "Senhas não conferem";
    private static final String BLANK_FIELD_MSG = "não pode ser vazio";
    public static final String LAST_NAME_FIELD = "Sobrenome";
    public static final String NAME_FIELD = "Nome";
    public static final String EMAIL_FIELD = "Email";
    public static final String PASSWORD_FIELD = "Senha";
    public static final String PASSWORD_CONFIRM_FIELD = "Confirmação da Senha";
    public static final String BIRTHNAME_FIELD = "Nascimento";
    private final Resources resources;
    private final Facade facade;
    private Activity activity;
    private final AutoCompleteTextView emailText;
    private final EditText passwordText;
    private final EditText cpfText;
    private final boolean edit;

    private boolean valid;

    private String password;
    private String name;
    private String email;
    private String lastName;
    private String confirmPassword;
    private String birth;
    private String cause;
    private String cpf;

    private String gender;

    @InjectView(R.id.et_name)
    EditText etName;

    @InjectView(R.id.et_lastname)
    @Optional
    EditText etLastName;

    @InjectView(R.id.et_confirm_password)
    @Optional
    EditText etPasswordConfirmation;

    @InjectView(R.id.et_cpf)
    @Optional
    EditText etCpf;

    @InjectView(R.id.et_birth)
    EditText etBirth;

    @InjectView(R.id.spinner)
    Spinner spinner;

    @InjectView(R.id.scroll)
    @Optional
    ScrollView scroll;

    private String genderSelected;
    private Calendar cal = Calendar.getInstance();
    private ArrayAdapter<String> genderAdapter;
    private boolean scrolled = false;

    public SignupHelper(Activity signupActivity,
                        AutoCompleteTextView emailText,
                        EditText passwordText, EditText cpfText) {
        this(signupActivity, emailText, passwordText, cpfText, false);
    }

    public SignupHelper(Activity signupActivity,
                        AutoCompleteTextView emailText,
                        EditText passwordText, EditText cpfText, boolean edit) {
        this.activity = signupActivity;
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.cpfText = cpfText;
        resources = activity.getResources();
        scrolled = false;

        facade = Facade.getInstance();

        ButterKnife.inject(this, activity);
        this.edit = edit;

        Typeface externalFont = Typeface.createFromAsset(activity.getAssets(), "fonts/regular.otf");
        emailText.setTypeface(externalFont);

        if(edit) {
            registerEditTextFocus();
        }

        createView();
    }

    private void registerEditTextFocus() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                scroll.post(new Runnable() {
                    public void run() {
                        scrollToName();
                    }
                });
            }
        };
        etName.setOnClickListener(clickListener);
        etBirth.setOnClickListener(clickListener);
        emailText.setOnClickListener(clickListener);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scrollToName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void scrollToName() {
        if(!scrolled) {
            scroll.scrollTo(0, etName.getTop());
            scrolled = true;
        }
    }

    public String getValidationError() {
        return cause;
    }

    public MobiClubUser getUser() {
        return createUser();
    }

    public void createView() {
        String[] genders = resources.getStringArray(R.array.gender_values);

        genderAdapter = createAdapter(genders);

        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderAdapter);
        spinner.setSelection(genderAdapter.getCount());
        spinner.setOnItemSelectedListener(this);

        dateInputListener();

        if(edit) {
            populateEdit();
        }
    }

    private void populateEdit() {
        MobiClubUser loggedUser = facade.getLoggedUser();
        if(loggedUser == null)
            return;
        String userName = loggedUser.getFullName();
        if(userName != null)
            etName.setText(userName);
        String userEmail = loggedUser.getEmail();
        if(userEmail != null)
        emailText.setText(userEmail);
        String birthday = loggedUser.getBirthdayString();
        if(birthday != null) {
            etBirth.setText(birthday);
        }
        String cpf = loggedUser.getCpf();
        if(cpf != null) {
            etCpf.setText(cpf);
        }

        String gender = loggedUser.getGenderString();
        if(gender != null) {
            int position = getGenderPostion(gender);
            spinner.setSelection(position);
        }
    }

    private int getGenderPostion(String genderType) {
        return genderAdapter.getPosition(genderType);
    }

    private ArrayAdapter<String> createAdapter(final String[] genders) {
        return new ArrayAdapter<String>(activity,
                    R.layout.spinner_item, genders) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    TextView tv = (TextView) v;
                    tv.setPadding(10,0,0,0);
                    Typeface externalFont = Typeface.createFromAsset(activity.getAssets(), "fonts/regular.otf");
                    tv.setTypeface(externalFont);
                    if (position == getCount()) {
                        ((TextView)v.findViewById(R.id.text_spinner)).setText("");
                        ((TextView)v.findViewById(R.id.text_spinner)).setHint(getItem(getCount())); //"Hint to be displayed"
                    }
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/regular.otf");
                    view.setTypeface(font);
                    return view;
                }

                @Override
                public int getCount() {
                    return super.getCount() - 1; // you dont display last item. It is used as hint.
                }

            };
    }

    private void dateInputListener() {
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    current = clean;
                    etBirth.setText(current);
                    etBirth.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etBirth.addTextChangedListener(tw);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderSelected = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isValid() {
        if(stringValid(NAME_FIELD, name) && stringValid(LAST_NAME_FIELD, lastName) &&
                stringValid(EMAIL_FIELD, email) && stringValid(PASSWORD_FIELD, password) &&
                stringValid(PASSWORD_CONFIRM_FIELD, confirmPassword) &&
                stringValid(BIRTHNAME_FIELD, birth) &&
                genderValid(gender) &&
                birthValid(birth) && passwordValid()) {
            return true;
        }
        return false;
    }

    private boolean passwordValid() {
        if(edit) {
            return true;
        }
        if(stringValid("name", password) && stringValid("name", confirmPassword) &&
                password.equals(confirmPassword)) {
            return true;
        } else {
            cause = PASSWORD_DOENST_MATCH;
            clearPassword();
            return false;
        }
    }

    private boolean birthValid(String birth) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
            Date date = parser.parse(birth);
            return true;
        } catch (ParseException e) {
        }
        cause = INVALID_BIRTH_DATE;
        return false;
    }

    private boolean genderValid(String gender) {
        if(gender == null) {
            cause = GENDER_INVALID;
            return false;
        }
        if(gender != null && gender.equals("Sexo") || gender.isEmpty()) {
            cause = GENDER_INVALID;
            return false;
        }
        return true;
    }

    private boolean stringValid(String field, String string) {
        if(edit && (field.equals(PASSWORD_CONFIRM_FIELD) ||
                    field.equals(PASSWORD_FIELD) ||
                    field.equals(LAST_NAME_FIELD))) {
            return true;
        }
        if(string != null && !string.isEmpty()) {
            return true;
        }
        cause = "Campo " + field + " " + BLANK_FIELD_MSG;
        return false;
    }

    private MobiClubUser createUser() {
        name = getName();
        email = getEmail();
        lastName = getLastName();
        if(lastName == null) {
            //se nao digitou entao esta alterando
            String[] names = name.split(" ");
            name = names[0];
            lastName = "";
            for(int i = 1;i < names.length;i++) {
                lastName += names[i];
                if(i < names.length - 1) {
                    lastName += " ";
                }
            }
        }
        gender = getGenderType();
        password = getPassword();
        confirmPassword = getPasswordConfirmation();
        birth = getBirth();
        cpf = getCpf();

        Date birthDate = Util.parseDate(birth);
        if(isValid())
            return new MobiClubUser(name, lastName, email, birthDate, gender, cpf);
        return null;
    }

    //refatorar isso
    public String getName() {
        return etName.getText().toString();
    }
    public String getCpf() {
        return etCpf.getText().toString();
    }
    public String getEmail() {
        return emailText.getText().toString();
    }

    public String getPassword() {
        if(passwordText == null) {
            return null;
        }
        return passwordText.getText().toString();
    }

    public String getLastName() {
        if(etLastName == null)
            return null;
        return etLastName.getText().toString();
    }

    public String getGenderType() {
        return getGenderTypeFromString(genderSelected);
    }

    private String getGenderTypeFromString(String string) {
        if(string.equals(MobiClubUser.GENDER_FEMALE_STRING)) {
            return MobiClubUser.GENDER_FEMALE_TYPE;
        } else if(string.equals(MobiClubUser.GENDER_MALE_STRING)) {
            return MobiClubUser.GENDER_MALE_TYPE;
        } else {
            return "";
        }
    }

    public String getPasswordConfirmation() {
        if(etPasswordConfirmation == null)
            return null;
        return etPasswordConfirmation.getText().toString();
    }

    public String getBirth() {
        return etBirth.getText().toString();
    }

    public void clearPassword() {
        etPasswordConfirmation.setText("");
        passwordText.setText("");
    }
}
