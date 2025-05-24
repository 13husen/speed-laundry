<?php

namespace App\Rules;

use App\Models\User;
use Illuminate\Contracts\Validation\Rule;

class InvalidOldPassword implements Rule
{
    private $userId;
    private $message;

    /**
     * Create a new rule instance.
     *
     * @param string $userId
     */
    public function __construct($userId = '')
    {
        $this->userId = $userId;
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
        $currentUser = User::find($this->userId);
        if (decrypt(($currentUser->password ?? '')) != $value) {
            $this->message = "Invalid Old Password !";
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
