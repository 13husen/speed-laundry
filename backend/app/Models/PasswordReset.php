<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class PasswordReset extends Model
{
    use Notifiable;

    protected $guarded = [];

    protected $dates = [
        'expired_at',
        'created_at',
        'updated_at',
    ];
}
