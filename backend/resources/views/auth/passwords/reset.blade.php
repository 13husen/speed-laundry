@extends('layouts.main')

@section('main-content')
  <div class="container">
    <div class="row justify-content-center m-2">
      <div class="col-md-12 text-center">
    <!-- Logo Here        
     <a href="{{ url('/') }}">
              <img src="{{ asset('img/logo.png') }}"
                   style="" height="50" alt="PicknGo">
            </a> -->
      </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-header">Reset Password</div>
          @if (isset($status))
            @if ($status == "error")
              @if (isset($detail))
                @foreach($detail as $data)
                      <div class="error text-danger">{{ $data[0] }}</div>
                @endforeach
              @endif
            @endif
          @endif
          <div class="card-body">
            <form method="POST" action="{{ route('user.reset-password-web') }}">
              @csrf
              <input type="hidden" name="token" value="{{ $token ?? '' }}">
              <input type="hidden" name="type" value="{{ $type ?? '' }}">
              <input type="hidden" name="email" value="{{ $email ?? '' }}">
              <div class="form-group row">
                <label for="password" class="col-md-4 col-form-label text-md-right">{{ __('Password') }}</label>

                <div class="col-md-6">
                  <input id="password" type="password" class="form-control
                  {{ !empty(session('errorDetails')['password'])? ' is-invalid' : '' }}" name="password"
                         value="{{ old('password') }}" required autofocus>
                </div>
              </div>

              <div class="form-group row">
                <label for="c_password" class="col-md-4 col-form-label text-md-right">{{ __('Confirm Password') }}</label>

                <div class="col-md-6">
                  <input id="c_password" type="password" class="form-control
                  {{ !empty(session('errorDetails')['c_password'])? ' is-invalid' : '' }}" name="c_password"
                         value="{{ old('c_password') }}" required autofocus>
                </div>
              </div>

              <div class="form-group row mb-0">
                <div class="col-md-6 offset-md-4">
                  <button type="submit" class="btn btn-primary">
                    {{ __('Reset Password') }}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection
