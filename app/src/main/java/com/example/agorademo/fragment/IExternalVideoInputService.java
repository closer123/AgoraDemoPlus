/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.agorademo.fragment;
public interface IExternalVideoInputService extends android.os.IInterface
{
  /** Default implementation for IExternalVideoInputService. */
  public static class Default implements IExternalVideoInputService
  {
    // the type of external video input is one of
    // ExternalVideoInputManager TYPE_LOCAL_VIDEO,
    // TYPE_SCREEN_SHARE
    // Bundle contains any information that is
    // necessary for this external video input
    // returns true if the input has been set or
    // has replaced the current input, false otherwise.

    @Override public boolean setExternalVideoInput(int type, android.content.Intent intent) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements IExternalVideoInputService
  {
    private static final String DESCRIPTOR = "io.agora.advancedvideo.externvideosource.IExternalVideoInputService";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an io.agora.advancedvideo.externvideosource.IExternalVideoInputService interface,
     * generating a proxy if needed.
     */
    public static IExternalVideoInputService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof IExternalVideoInputService))) {
        return ((IExternalVideoInputService)iin);
      }
      return new Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_setExternalVideoInput:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          android.content.Intent _arg1;
          if ((0!=data.readInt())) {
            _arg1 = android.content.Intent.CREATOR.createFromParcel(data);
          }
          else {
            _arg1 = null;
          }
          boolean _result = this.setExternalVideoInput(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements IExternalVideoInputService
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      // the type of external video input is one of
      // ExternalVideoInputManager TYPE_LOCAL_VIDEO,
      // TYPE_SCREEN_SHARE
      // Bundle contains any information that is
      // necessary for this external video input
      // returns true if the input has been set or
      // has replaced the current input, false otherwise.

      @Override public boolean setExternalVideoInput(int type, android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(type);
          if ((intent!=null)) {
            _data.writeInt(1);
            intent.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_setExternalVideoInput, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setExternalVideoInput(type, intent);
          }
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static IExternalVideoInputService sDefaultImpl;
    }
    static final int TRANSACTION_setExternalVideoInput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(IExternalVideoInputService impl) {
      if (Proxy.sDefaultImpl == null && impl != null) {
        Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static IExternalVideoInputService getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
  }
  // the type of external video input is one of
  // ExternalVideoInputManager TYPE_LOCAL_VIDEO,
  // TYPE_SCREEN_SHARE
  // Bundle contains any information that is
  // necessary for this external video input
  // returns true if the input has been set or
  // has replaced the current input, false otherwise.

  public boolean setExternalVideoInput(int type, android.content.Intent intent) throws android.os.RemoteException;
}
