<?php

namespace App\Models;

use App\Traits\ModelFilterTrait;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class Transaction extends Model
{
    use Notifiable, ModelFilterTrait;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $with = ['payment','tipe'];
    protected $dates = [
        'created_at',
        'updated_at',
        'deleted_at',
        'job_done_at',
    ];
    protected $casts = [
       'created_at' => 'datetime:Y-m-d h:i:s',
       'updated_at' => 'datetime:Y-m-d h:i:s',
       'deleted_at' => 'datetime:Y-m-d h:i:s',
       'job_done_at' => 'datetime:Y-m-d h:i:s'
    ];    
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];


    public function payment()
    {
        return $this->belongsTo(Payment::class, 'payment_id');
    }

    public function transactionDetail()
    {
        return $this->hasMany(TransactionDetail::class, 'transaction_id');
    }

    public function tipe()
    {
        return $this->belongsTo(TypeLaundry::class, 'type_id')
        ->select(['id', 'nama','fee']);
    }


    public function user()
    {
        return $this->belongsTo(User::class, 'user_id');        
    }


    public function courier()
    {
        return $this->belongsTo(User::class, 'courier_id')
            ->select(['id', 'name', 'email', 'type']);
    }

    /**
     * @param $query
     * @param array $params
     * @return mixed
     */
    public function scopeSearch($query, array $params = [])
    {
        $query = $this->likeEmpty('user_id', $params, $query);
        $query = $this->likeEmpty('invoice', $params, $query);
        $query = $this->likeEmpty('courier_id', $params, $query);
        $query = $this->likeEmpty('payment_id', $params, $query);
        $query = $this->likeEmpty('status', $params, $query);
        $query = $this->likeEmpty('type', $params, $query);
        $query = $this->whereEmpty('is_handled', $params, $query);

        $query = $this->likeNullRelation('payment', 'status', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        $query->where(function ($subQuery) use ($params) {
            $subQuery = $this->universalSearch($params, $subQuery, [ 'name', 'code',]);
            $subQuery = $this->universalSearch($params, $subQuery, ['status'], 'payment');            
            return $subQuery;
        });

        return $query;
    }

    /**
     * @param $query
     * @param array $params
     * @return mixed
     */
    public function scopeMatch($query, array $params = [])
    {
        $query = $this->whereEmpty('user_id', $params, $query);
        $query = $this->whereEmpty('invoice', $params, $query);
        $query = $this->whereEmpty('courier_id', $params, $query);
        $query = $this->whereEmpty('payment_id', $params, $query);
        $query = $this->whereEmpty('status', $params, $query);
        $query = $this->whereEmpty('type', $params, $query);
        $query = $this->whereEmpty('is_handled', $params, $query);

        $query = $this->whereEmptyRelation('payment', 'status', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }
}
