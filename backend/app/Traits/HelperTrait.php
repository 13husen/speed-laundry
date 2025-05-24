<?php
namespace App\Traits;
use App\Models\Transaction;
use App\Models\User;
use App\Models\Notification;
use App\Enums\TransactionList;
use Kreait\Firebase\Factory;
use Kreait\Firebase\Messaging\CloudMessage;
use Kreait\Firebase\Messaging\Notification as FirebaseNotification;

trait HelperTrait
{

	function sendPushNotif($message, User $user, Transaction $transaction, $userType = "user")
	{
		try {
			$notifData = [
				"user_id" => $user->id,
				"transaction_id" => $transaction->id,
				"invoice" => $transaction->invoice,
				"message" => $message,
				"type" => $transaction->type,
				"user_type" => $userType,
			];
			if ($userType == "admin") {
				$userAdmin = User::where("type", "admin")->get()->toArray();
				$response = [];
				foreach ($userAdmin as $adminItem) {
					if ($adminItem['fcm_token'] != null) {
						$title = $transaction->type == TransactionList::ACTION_TOPUP
							? "Topup Wallet"
							: "Transaksi Laundry";

						$body = "transaksi " . $transaction->invoice . " " . $message;

						$fcmToken = $adminItem['fcm_token'];
						$serviceAccountPath = config('laundry.fcm_key.fcm_service_account_path_admin');

						$response = $this->callPushFCM($fcmToken, $title, $body, $notifData, $serviceAccountPath);
					}
				}
				$notifData['request_data'] = json_encode([
					"fcmToken" => $fcmToken,
					"title" => $title,
					"body" => $body,
					"notifData" => $notifData,
				]);
			} else {
				$title = $transaction->type == TransactionList::ACTION_TOPUP
					? "Topup Wallet"
					: "Transaksi Laundry";

				$body = "transaksi " . $transaction->invoice . " " . $message;

				$fcmToken = $user->fcm_token;
				$serviceAccountPath = config('laundry.fcm_key.fcm_service_account_path');
				$response = $this->callPushFCM($fcmToken, $title, $body, $notifData, $serviceAccountPath);

				$notifData['response_data'] = json_encode($response);
				$notifData['request_data'] = json_encode([
					"fcmToken" => $fcmToken,
					"title" => $title,
					"body" => $body,
					"notifData" => $notifData,
				]);

			}
			unset($notifData['invoice']);
			Notification::create($notifData);
			return $response;
		} catch (Exception $exception) {
			return $this->responseError($exception->getMessage());
		}
	}


	function callPushFCM(string $fcmToken, string $title, string $body, array $data = [],  string $serviceAccountPath)
	{
		// $projectId = config('laundry.fcm_key.fcm_project_id');

		$factory = (new Factory)->withServiceAccount($serviceAccountPath);
		$messaging = $factory->createMessaging();
	
		$notification = FirebaseNotification::create($title, $body);
	
		$message = CloudMessage::withTarget('token', $fcmToken)
			->withNotification($notification)
			->withData($data);
	
		return $messaging->send($message);
	}


	function distance($lat1, $lon1, $unit)
	{
		$lat2 = config('laundry.store.lat');
		$lon2 = config('laundry.store.long');
		if (($lat1 == $lat2) && ($lon1 == $lon2)) {
			return 0;
		} else {
			$theta = $lon1 - $lon2;
			$dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
			$dist = acos(min(max($dist, -1.0), 1.0));
			$dist = rad2deg($dist);
			$miles = $dist * 60 * 1.1515;
			$unit = strtoupper($unit);

			if ($unit == "K") { //kilometers
				return ($miles * 1.609344);
			} else if ($unit == "N") {  // nautical miles
				return ($miles * 0.8684);
			} else {
				return $miles; // miles
			}
		}
	}

}
