package com.example.userform.lead

import com.example.userform.designsystem.atoms.SelectOption
import com.example.userform.designsystem.form.FieldConfig
import com.example.userform.designsystem.form.FieldSpan
import com.example.userform.designsystem.form.FieldType
import com.example.userform.designsystem.form.Validation
import com.example.userform.designsystem.form.VisibleWhen

val LeadFormConfig: List<FieldConfig> = listOf(
    FieldConfig(
        name = "fullName",
        type = FieldType.Text,
        label = "Full name",
        validations = listOf(Validation.Required),
        span = FieldSpan.Half,
    ),
    FieldConfig(
        name = "email",
        type = FieldType.Email,
        label = "Email",
        validations = listOf(Validation.Required, Validation.Email),
        span = FieldSpan.Half,
    ),
    FieldConfig(
        name = "leadType",
        type = FieldType.Select,
        label = "Lead type",
        validations = listOf(Validation.Required),
        options = listOf(
            SelectOption(value = "individual", label = "Individual"),
            SelectOption(value = "company", label = "Company"),
        ),
        span = FieldSpan.Half,
    ),
    FieldConfig(
        name = "companyName",
        type = FieldType.Text,
        label = "Company name",
        validations = listOf(Validation.Required),
        visibleWhen = VisibleWhen(field = "leadType", equals = "company"),
        span = FieldSpan.Half,
    ),
    FieldConfig(
        name = "phone",
        type = FieldType.Text,
        label = "Phone",
        validations = listOf(Validation.Required, Validation.Digits(10)),
        hint = "10 digits, numbers only",
        span = FieldSpan.Half,
    ),
    FieldConfig(
        name = "notes",
        type = FieldType.TextArea,
        label = "Notes",
        validations = listOf(Validation.MaxLength(200)),
        hint = "Optional, up to 200 characters",
    ),
    FieldConfig(
        name = "consent",
        type = FieldType.Checkbox,
        label = "I agree to be contacted about this enquiry",
        validations = listOf(Validation.Required),
    )
)
