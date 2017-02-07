# Remove verbose and debug logging in release versions
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
