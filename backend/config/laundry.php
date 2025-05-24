<?php

return [
    'fee' => [
        'shipping' => 5000,
        'admin' => 1000,
    ],
    "store" => [
        'max_distance' => 50,
        'lat' => -6.25817393899093,
        'long' => 107.07334548127007,
    ],
    'fcm_key' => [
    	'admin' => "AAAAd-NKYU0:APA91bHrBLCObaFSQ8iGphMB4DNY7YXl7ietX6ekgV06w-eyK4jJfO1SN_zDf41I14wsLZydvVpE5xE3ewRul8vSPoTGAWjJoQR1yZb0WzUZJuB7xqndoMNgn1StufjmO3ndTiLT6aw0",
    	'user' => "AAAA1q01mIc:APA91bEqaW7A7f3Y7azX80g-klRNnGm2hIffddXnSeucQmx3gvWotj6tdWyI3MCLVBdU7xeKZ9G9vw8lXSTw2XMLra8aHILB_-AaHoLX031BbgEcTIZyHqy1oh6zjOuKP6DfYLdnahc8",
        'fcm_project_id' => "speed-laundry",
        'fcm_service_account_path' =>  storage_path('app/' . "firebase/speed-laundry-firebase-adminsdk-1soev-516a773801.json"),
        'fcm_service_account_path_admin' =>  storage_path('app/' . "firebase/speed-laundry-admin-firebase-adminsdk-qdukp-8fd8c00915.json"),

    ]
];
