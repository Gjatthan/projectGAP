package com.example.gap.misc;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class Biometric {
    ConstraintLayout layout;
    BiometricPrompt bioprompt;
    BiometricPrompt.PromptInfo promptInfo;
    Context mcontext;


    public Biometric(Context mcontext) {
        this.mcontext = mcontext;
    }

    public boolean isAuthenticateSuccess() {
        final boolean[] success = {false};
        BiometricManager biometricManager = BiometricManager.from(mcontext);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(mcontext, "hardware not found", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(mcontext, "hardware not enrolled", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(mcontext, "no hardware", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                Executor executor = ContextCompat.getMainExecutor(mcontext);
                bioprompt = new BiometricPrompt((FragmentActivity) mcontext, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        success[0] =true;
                        Toast.makeText(mcontext, ""+success[0], Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login using Biometric")
//                        .setConfirmationRequired(true)
//                        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                        .setDeviceCredentialAllowed(true)
                        .build();
                bioprompt.authenticate(promptInfo);
        }
        return success[0];
    }
}
