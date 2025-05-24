<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class TransactionClothe extends Model
{
    use Notifiable;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $with = ['categorizeClothe'];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];

    public function categorizeClothe()
    {
        return $this->belongsTo(CategorizeClothe::class, 'clothe_id')
        ->select(['id', 'clothe','fee']);
    }    
}
