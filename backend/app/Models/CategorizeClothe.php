<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use App\Traits\ModelFilterTrait;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Notifications\Notifiable;

class CategorizeClothe extends Model
{
    use Notifiable, ModelFilterTrait, SoftDeletes;
    protected $guard_name = 'api';
    protected $guarded = [];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        //
    ];

    protected $dates = [
        'created_at',
        'updated_at',
        'deleted_at',
    ];

    /**
     * @param $query
     * @param array $params
     * @return mixed
     */

    public function scopeSearch($query, array $params = [])
    {
        $query = $this->likeEmpty('id', $params, $query);
        $query = $this->likeEmpty('clothe', $params, $query);
        $query = $this->likeEmpty('status', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        $query->where(function ($subQuery) use ($params) {
            $subQuery = $this->universalSearch($params, $subQuery, ['id', 'name', 'status',]);
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
        $query = $this->whereEmpty('id', $params, $query);
        $query = $this->whereEmpty('clothe', $params, $query);
        $query = $this->whereEmpty('status', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }
}
