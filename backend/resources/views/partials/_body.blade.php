<script>
	   var KTAppOptions = {
		"colors":{
		"state":{
		"brand":"#5d78ff",
		"dark":"#282a3c",
		"light":"#ffffff",
		"primary":"#5867dd",
		"success":"#34bfa3",
		"info":"#36a3f7",
		"warning":"#ffb822",
		"danger":"#fd3995"},
		"base":{
		"label":[
		"#c5cbe3",
		"#a1a8c3",
		"#3d4465",
		"#3e4466"],
		"shape":[
		"#f0f3ff",
		"#d9dffa",
		"#afb4d4",
		"#646c9a"]}}};        
	</script> 
    <!-- end::Global Config -->

	 <!--begin::Global Theme Bundle(used by all pages) -->	 
	<script src="{{ asset('assets/vendors/base/vendors.bundle.js') }}" type="text/javascript"></script> 
	<script src="{{ asset('assets/demo/default/base/scripts.bundle.js') }}" type="text/javascript"></script> 
    <!--end::Global Theme Bundle -->
	
    <!--begin::Page Vendors(used by this page) -->	 
	<!-- <script src="{{ asset('assets/vendors/custom/fullcalendar/fullcalendar.bundle.js') }}" type="text/javascript"></script>  -->
	<!--script src="//maps.google.com/maps/api/js?key=AIzaSyBTGnKT7dt597vo9QgeQ7BFhvSRP4eiMSM" type="text/javascript"></script--> 
	<!--script src="./assets/vendors/custom/gmaps/gmaps.js" type="text/javascript"></script--> 
    <!--end::Page Vendors -->
    <!--begin::Page Scripts(used by this page) -->
	 
	<!--script src="{{ asset('assets/app/custom/general/dashboard.js') }}" type="text/javascript"></script--> 
    <!--end::Page Scripts -->
	<!--begin::Global App Bundle(used by all pages) -->
	<!--script src="{{ asset('assets/app/custom/general/components/extended/bootstrap-notify.js') }}" type="text/javascript"></script-->	
	<!-- <script src="{{ asset('plugins/izitoast/js/iziToast.min.js') }}" type="text/javascript"></script>	
	<script src="{{ asset('plugins/tooltipster/js/tooltipster.bundle.min.js') }}" type="text/javascript"></script>	 -->
	<script src="{{ asset('assets/app/bundle/app.bundle.js') }}" type="text/javascript"></script> 
	<!--end::Global App Bundle -->
	<!--begin::Custom Script -->
	
	<!--end::Custom Script -->	
	
	@stack('js-end')