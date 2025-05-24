<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class TransactionDetail extends Model
{
    use Notifiable;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $with = ["kategori","pakaian"];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];

    public function kategori()
    {
        return $this->belongsTo(Category::class, 'category_id')
        ->select(['id', 'name','fee']);
    }


    public function pakaian()
    {
        return $this->hasMany(TransactionClothe::class, 'transaction_detail_id');
    }


}
