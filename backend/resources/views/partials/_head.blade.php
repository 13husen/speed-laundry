<meta charset="utf-8">
<title>{{ config('app.name', 'My App') }}</title>
<meta content="Dashboard" name="description">
<meta content="width=device-width, initial-scale=1, shrink-to-fit=no" name="viewport">
<!-- CSRF Token -->
<meta name="csrf-token" content="{{ csrf_token() }}">

<!--begin::Fonts -->
<script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.16/webfont.js">
</script>
<script>
    WebFont.load({
        google: {
            "families":[
            "Poppins:300,400,500,600,700",
            "Roboto:300,400,500,600,700"]},
        active: function() {
            sessionStorage.fonts = true;                
        }           
    });        
</script>
<!--end::Fonts -->
<!--begin::Page Vendors Styles(used by this page) -->
<!-- <link href="{{ asset('assets/vendors/custom/fullcalendar/fullcalendar.bundle.css') }}" rel="stylesheet" type="text/css"> -->
<!--end::Page Vendors Styles -->
<!--begin::Global Theme Styles(used by all pages) -->
<link href="{{ asset('assets/vendors/base/vendors.bundle.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('assets/demo/default/base/style.bundle.css') }}" rel="stylesheet" type="text/css">
<!--end::Global Theme Styles -->
<!--begin::Layout Skins(used by all pages) -->
<link href="{{ asset('assets/demo/default/skins/header/base/light.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('assets/demo/default/skins/header/menu/light.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('assets/demo/default/skins/brand/dark.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('assets/demo/default/skins/aside/dark.css') }}" rel="stylesheet" type="text/css">
<!-- <link href="{{ asset('plugins/izitoast/css/iziToast.min.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('plugins/tooltipster/css/tooltipster.bundle.min.css') }}" rel="stylesheet" type="text/css">
<link href="{{ asset('plugins/tooltipster/css/plugins/tooltipster/sideTip/themes/tooltipster-sideTip-shadow.min.css') }}" rel="stylesheet" type="text/css"> -->
<!--link href="{{ asset('assets/css/all.css') }}" rel="stylesheet" type="text/css"-->	
<!--end::Layout Skins -->
<!--begin::Custom style -->

<!--end::Custom style -->
@stack('css-head')
<link href="{{ asset('img/favicon.ico') }}" rel="shortcut icon">

<!-- Global site tag (gtag.js) - Google Analytics -->
<!--script async src="https://www.googletagmanager.com/gtag/js?id=UA-XXXXXXXX-1"></script-->
<!--script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }
    gtag('js', new Date());
    gtag('config', 'UA-XXXXXXXX-1');
</script-->