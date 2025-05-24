<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
<!-- begin::Head -->
<head>
@include('partials._head')
</head><!-- end::Head -->
<!-- begin::Body -->
<body class="kt-page--loading-enabled kt-page--loading kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed kt-page--loading">
	@yield('partials._layout-page-loader')
	@yield('main-content')
	<!-- begin::Global Config(global config for global JS sciprts) -->
	@include('partials._body')
	<!-- end::Body -->
</body>
</html>