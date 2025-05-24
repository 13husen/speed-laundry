@extends('layouts.main')
@section('main-content')
<div class="kt-grid kt-grid--ver kt-grid--root">
    <div class="kt-grid kt-grid--hor kt-grid--root  kt-login kt-login--v3 kt-login--signin" id="kt_login">
        <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor" style="background-image: url({{ asset('assets/media/bg/bg-3.jpg') }});">
            <div class="kt-grid__item kt-grid__item--fluid kt-login__wrapper">
                <div class="kt-login__container">
                    <div class="kt-login__logo">
                        <a href="{{ route('home') }}">
                            <img src="{{ asset('img/logo.png') }}" style="width: 120px;border-radius: 12px">
                        </a>
                    </div>
                    @yield('content')                    
                </div>
            </div>
        </div>
    </div>
</div>
@endsection

@push('css-head')
    <link href="{{ asset('assets/app/custom/login/login-v3.default.css') }}" rel="stylesheet" type="text/css">
@endpush

@push('js-end')
    <script src="{{ asset('assets/app/custom/login/login-general.js') }}"></script>
@endpush