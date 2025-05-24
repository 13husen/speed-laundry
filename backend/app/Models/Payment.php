<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class Payment extends Model
{
    use Notifiable;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $dates = [
        'created_at',
        'updated_at',
        'deleted_at',
    ];
    protected $casts = [
       'created_at' => 'datetime:Y-m-d h:i:s',
       'updated_at' => 'datetime:Y-m-d h:i:s',
       'deleted_at' => 'datetime:Y-m-d h:i:s',
    ];    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];

}
