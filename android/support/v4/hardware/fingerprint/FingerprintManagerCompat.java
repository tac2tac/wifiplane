// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.os.Handler;
import android.support.v4.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

// Referenced classes of package android.support.v4.hardware.fingerprint:
//            FingerprintManagerCompatApi23

public final class FingerprintManagerCompat
{
    private static class Api23FingerprintManagerCompatImpl
        implements FingerprintManagerCompatImpl
    {

        private static CryptoObject unwrapCryptoObject(FingerprintManagerCompatApi23.CryptoObject cryptoobject)
        {
            CryptoObject cryptoobject1 = null;
            if(cryptoobject != null) goto _L2; else goto _L1
_L1:
            return cryptoobject1;
_L2:
            if(cryptoobject.getCipher() != null)
                cryptoobject1 = new CryptoObject(cryptoobject.getCipher());
            else
            if(cryptoobject.getSignature() != null)
                cryptoobject1 = new CryptoObject(cryptoobject.getSignature());
            else
            if(cryptoobject.getMac() != null)
                cryptoobject1 = new CryptoObject(cryptoobject.getMac());
            if(true) goto _L1; else goto _L3
_L3:
        }

        private static FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(AuthenticationCallback authenticationcallback)
        {
            return new FingerprintManagerCompatApi23.AuthenticationCallback(authenticationcallback) {

                public void onAuthenticationError(int i, CharSequence charsequence)
                {
                    callback.onAuthenticationError(i, charsequence);
                }

                public void onAuthenticationFailed()
                {
                    callback.onAuthenticationFailed();
                }

                public void onAuthenticationHelp(int i, CharSequence charsequence)
                {
                    callback.onAuthenticationHelp(i, charsequence);
                }

                public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal authenticationresultinternal)
                {
                    callback.onAuthenticationSucceeded(new AuthenticationResult(Api23FingerprintManagerCompatImpl.unwrapCryptoObject(authenticationresultinternal.getCryptoObject())));
                }

                final AuthenticationCallback val$callback;

            
            {
                callback = authenticationcallback;
                super();
            }
            }
;
        }

        private static FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(CryptoObject cryptoobject)
        {
            FingerprintManagerCompatApi23.CryptoObject cryptoobject1 = null;
            if(cryptoobject != null) goto _L2; else goto _L1
_L1:
            return cryptoobject1;
_L2:
            if(cryptoobject.getCipher() != null)
                cryptoobject1 = new FingerprintManagerCompatApi23.CryptoObject(cryptoobject.getCipher());
            else
            if(cryptoobject.getSignature() != null)
                cryptoobject1 = new FingerprintManagerCompatApi23.CryptoObject(cryptoobject.getSignature());
            else
            if(cryptoobject.getMac() != null)
                cryptoobject1 = new FingerprintManagerCompatApi23.CryptoObject(cryptoobject.getMac());
            if(true) goto _L1; else goto _L3
_L3:
        }

        public void authenticate(Context context, CryptoObject cryptoobject, int i, CancellationSignal cancellationsignal, AuthenticationCallback authenticationcallback, Handler handler)
        {
            FingerprintManagerCompatApi23.CryptoObject cryptoobject1 = wrapCryptoObject(cryptoobject);
            if(cancellationsignal != null)
                cryptoobject = ((CryptoObject) (cancellationsignal.getCancellationSignalObject()));
            else
                cryptoobject = null;
            FingerprintManagerCompatApi23.authenticate(context, cryptoobject1, i, cryptoobject, wrapCallback(authenticationcallback), handler);
        }

        public boolean hasEnrolledFingerprints(Context context)
        {
            return FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }

        public boolean isHardwareDetected(Context context)
        {
            return FingerprintManagerCompatApi23.isHardwareDetected(context);
        }


        public Api23FingerprintManagerCompatImpl()
        {
        }
    }

    public static abstract class AuthenticationCallback
    {

        public void onAuthenticationError(int i, CharSequence charsequence)
        {
        }

        public void onAuthenticationFailed()
        {
        }

        public void onAuthenticationHelp(int i, CharSequence charsequence)
        {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationresult)
        {
        }

        public AuthenticationCallback()
        {
        }
    }

    public static final class AuthenticationResult
    {

        public CryptoObject getCryptoObject()
        {
            return mCryptoObject;
        }

        private CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject cryptoobject)
        {
            mCryptoObject = cryptoobject;
        }
    }

    public static class CryptoObject
    {

        public Cipher getCipher()
        {
            return mCipher;
        }

        public Mac getMac()
        {
            return mMac;
        }

        public Signature getSignature()
        {
            return mSignature;
        }

        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature)
        {
            mSignature = signature;
            mCipher = null;
            mMac = null;
        }

        public CryptoObject(Cipher cipher)
        {
            mCipher = cipher;
            mSignature = null;
            mMac = null;
        }

        public CryptoObject(Mac mac)
        {
            mMac = mac;
            mCipher = null;
            mSignature = null;
        }
    }

    private static interface FingerprintManagerCompatImpl
    {

        public abstract void authenticate(Context context, CryptoObject cryptoobject, int i, CancellationSignal cancellationsignal, AuthenticationCallback authenticationcallback, Handler handler);

        public abstract boolean hasEnrolledFingerprints(Context context);

        public abstract boolean isHardwareDetected(Context context);
    }

    private static class LegacyFingerprintManagerCompatImpl
        implements FingerprintManagerCompatImpl
    {

        public void authenticate(Context context, CryptoObject cryptoobject, int i, CancellationSignal cancellationsignal, AuthenticationCallback authenticationcallback, Handler handler)
        {
        }

        public boolean hasEnrolledFingerprints(Context context)
        {
            return false;
        }

        public boolean isHardwareDetected(Context context)
        {
            return false;
        }

        public LegacyFingerprintManagerCompatImpl()
        {
        }
    }


    private FingerprintManagerCompat(Context context)
    {
        mContext = context;
    }

    public static FingerprintManagerCompat from(Context context)
    {
        return new FingerprintManagerCompat(context);
    }

    public void authenticate(CryptoObject cryptoobject, int i, CancellationSignal cancellationsignal, AuthenticationCallback authenticationcallback, Handler handler)
    {
        IMPL.authenticate(mContext, cryptoobject, i, cancellationsignal, authenticationcallback, handler);
    }

    public boolean hasEnrolledFingerprints()
    {
        return IMPL.hasEnrolledFingerprints(mContext);
    }

    public boolean isHardwareDetected()
    {
        return IMPL.isHardwareDetected(mContext);
    }

    static final FingerprintManagerCompatImpl IMPL;
    private Context mContext;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT >= 23)
            IMPL = new Api23FingerprintManagerCompatImpl();
        else
            IMPL = new LegacyFingerprintManagerCompatImpl();
    }
}
