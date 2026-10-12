# UserForm

A lead capture form for Android, built with Kotlin and Jetpack Compose. The screen is rendered from a config list, not written out field by field, and sits on top of a small design system of my own (no Material, no form library).

## Build and run

**Requirements**

- Android Studio with support for AGP 9.3
- JDK 21 (Gradle picks it up through the toolchain config)
- Android SDK platform 37 (`compileSdk = 37`, `minSdk = 26`)

**Android Studio**

1. Open the project folder and let Gradle sync.
2. Select the `app` run configuration.
3. Pick a device and press Run.

**Gradle**

```bash
# Windows
gradlew.bat assembleDebug
gradlew.bat installDebug

# macOS / Linux
./gradlew assembleDebug
./gradlew installDebug
```

The debug APK ends up in `app/build/outputs/apk/debug/`.

**Emulators**

- Compact (one column): a phone in portrait, e.g. Pixel 7, API 34 or newer.
- Expanded (two columns): Medium Tablet (API 34 or newer), or the same phone rotated to landscape. The switch happens once the form is at least 600dp wide.

## Package layout

```
com.example.userform
├── MainActivity.kt                 edge-to-edge setup, hosts LeadScreen inside CustomTheme
├── designsystem/                   generic, knows nothing about leads
│   ├── tokens/
│   │   ├── Colors.kt               CustomColorScheme + LightColorScheme
│   │   ├── CustomTheme.kt          provides the color scheme (CompositionLocal)
│   │   ├── Spacing.kt              CustomSpacing
│   │   ├── Typography.kt           CustomTypography
│   │   └── Breakpoints.kt          two-column width (600dp), max content width
│   ├── atoms/
│   │   ├── CustomTextField.kt      text, email and textarea
│   │   ├── CustomSelect.kt         dropdown + SelectOption
│   │   ├── CustomCheckbox.kt
│   │   ├── CustomButton.kt
│   │   └── FieldFrame.kt           bordered box shared by text field and select
│   ├── molecules/
│   │   └── CustomFormField.kt      label, control, hint and error
│   └── form/
│       ├── FormConfig.kt           FieldConfig, FieldType, Validation, VisibleWhen, FieldSpan
│       ├── DynamicForm.kt          maps each field type to an atom, picks the layout
│       └── FormLayouts.kt          compact and expanded layouts
└── lead/
    ├── LeadFormConfig.kt           the lead form fields
    ├── LeadValidation.kt           validate(config, values)
    ├── LeadFormState.kt            values, touched fields, submitted result
    └── LeadScreen.kt               screen: insets, scrolling, submit bar, submitted values
```

## Where things live

**Tokens** are in `designsystem/tokens/`. Spacing, typography and breakpoints are plain objects. Colors go through `CustomTheme` so a dark scheme could be dropped in later without touching any component.

**Atoms** are in `designsystem/atoms/`. Each one takes a value, a label and callbacks, and has no idea what a lead is. They are built on Compose foundation (`BasicTextField`, `Popup`, `Canvas`, etc.).

**The field molecule** is `designsystem/molecules/CustomFormField.kt`. It also keeps the focused field, error included, above the keyboard.

**The form** is `designsystem/form/DynamicForm.kt`. It takes the config, the current values and the errors, filters out hidden fields, and renders one atom per entry based on its `type`.

## Config and validation

**Config:** `lead/LeadFormConfig.kt`. Each entry has a `name`, `type`, `label` and `validations`, plus optional `options`, `hint`, `visibleWhen` and `span`. Company name uses `visibleWhen = VisibleWhen("leadType", "company")`, so it only shows up when Lead type is Company.

**Validation rules:** `lead/LeadValidation.kt`. Everything goes through one pure function:

```kotlin
fun validate(config: List<FieldConfig>, values: FormValues): Map<String, String>
```

It returns field name to error message. Hidden fields are skipped, which is how Company name is required only while it is visible. `LeadFormState` decides when errors are shown: after a field loses focus, and for every field on submit.

Adding a field means adding one entry to `LeadFormConfig`. Nothing else needs to change.

## Compact and expanded layouts

- `designsystem/form/FormLayouts.kt` owns both layouts.
  - `CompactFormLayout`: one column, every field full width.
  - `ExpandedFormLayout`: two columns. Consecutive `Half` fields share a row (Full name and Email, Lead type and Company name), while `Full` fields like Notes and Consent take the whole row.
- `designsystem/form/DynamicForm.kt` picks between them using `CustomBreakpoints.TwoColumn`.
- `lead/LeadScreen.kt` handles the rest of the responsive behaviour: content stays inside the system bars, the submit bar sits above the keyboard, and content is capped at 840dp on wide screens.
