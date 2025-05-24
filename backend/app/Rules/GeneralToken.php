<?php

namespace App\Rules;

use App\Enums\PasswordResetList;
use App\Models\PasswordReset;
use Carbon\Carbon;
use Illuminate\Contracts\Validation\Rule;

class GeneralToken implements Rule
{
    private $type;
    private $prefix;
    private $message;
    private $usernameField;
    private $username;

    /**
     * Create a new rule instance.
     *
     * @param string $type
     * @param string $usernameField
     * @param string $username
     * @param string $prefix
     */
    public function __construct($type = '', $usernameField = '', $username = '', $prefix = '')
    {
        $this->type = $type;
        $this->usernameField = $usernameField;
        $this->username = $username;
        $this->prefix = $prefix;
    }

    /**
     * Determine if the validation rule passes.
     *
     * @param string $attribute
     * @param mixed $value
     * @return bool
     */
    public function passes($attribute, $value)
    {
        $passwordReset = PasswordReset::where('token', $value)
            ->where($this->usernameField, $this->username)
            ->latest()->first();

        $now = Carbon::now();
        if (!$passwordReset) {
            $this->message = "Invalid Token";
            return false;
        } elseif ($now->greaterThan(Carbon::createFromFormat('Y-m-d H:i:s', $passwordReset->expired_at))) {
            $this->message = "Token Expired";
            return false;
        } elseif ($passwordReset->status == PasswordResetList::STATUS_USED) {
            $this->message = "Token Used";
            return false;
        }

        return true;
    }

    /**
     * Get the validation error message.
     *
     * @return string
     */
    public function message()
    {
        return $this->message;
    }
}
