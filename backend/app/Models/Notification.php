<?php

namespace App\Models;

use App\Traits\ModelFilterTrait;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class Notification extends Model
{
    use Notifiable, ModelFilterTrait;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $with = ['transaction'];
    protected $dates = [
        'created_at',
        'updated_at',
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];


    public function transaction()
    {
        return $this->belongsTo(Transaction::class, 'transaction_id');
    }

  /**
     * @param $query
     * @param array $params
     * @return mixed
     */
    public function scopeSearch($query, array $params = [])
    {
        $query = $this->likeEmpty('user_id', $params, $query);
        $query = $this->likeEmpty('transaction_id', $params, $query);
        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        $query->where(function ($subQuery) use ($params) {
            $subQuery = $this->universalSearch($params, $subQuery, [ 'user_id', 'transaction_id']);
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
        $query = $this->whereEmpty('transaction_id', $params, $query);
        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }    
}
