# Quick Fix for Date Parsing Issue

## Issue
The date field in the employee form is not parsing correctly, causing a 400 error when submitting the form.

## Root Cause
The date input field is sending an incorrect format to the backend. The backend expects YYYY-MM-DD format but receives a malformed date.

## Fix

### Option 1: Frontend Fix (Recommended)
Update the employee form component to properly format the date before sending to backend:

In `employee-form.component.ts`, modify the `onSubmit()` method:

```typescript
onSubmit() {
  if (this.employeeForm.valid) {
    const formData = { ...this.employeeForm.value };
    
    // Fix date format
    if (formData.dateBirth) {
      const date = new Date(formData.dateBirth);
      formData.dateBirth = date.toISOString().split('T')[0]; // YYYY-MM-DD format
    }
    
    // Continue with existing submission logic...
  }
}
```

### Option 2: Backend Fix
Add a custom date deserializer in the Employee entity:

```java
@JsonFormat(pattern = "yyyy-MM-dd")
@DateTimeFormat(pattern = "yyyy-MM-dd")
private LocalDate dateBirth;
```

## Testing the Fix
1. Apply the fix
2. Restart the application
3. Fill the form with a valid date (e.g., 1985-05-15)
4. Submit the form
5. Check admin dashboard for the new application

## Expected Result
After applying the fix, form submissions should work correctly and applications should appear in the admin dashboard with "EN_ATTENTE" status.

