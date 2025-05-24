<?php

namespace App\Traits;

use App\Models\User;

trait WalletTrait
{
    public function credit($nominal, User $user)
    {
		try {
			$newAmount = 0;
			$newAmount = $user->balance - $nominal;
			$user->balance = $newAmount;
			$user->save();
			return $newAmount;
		} catch (Exception $e) {
			return 0;
		}
    }

    public function debit($nominal, User $user)
    {
		try {
			$newAmount = 0;
			$newAmount = $user->balance + $nominal;
			$user->balance = $newAmount;
			$user->save();
			return $newAmount;
		} catch (Exception $e) {
			return 0;
		}
    }    

  
}
