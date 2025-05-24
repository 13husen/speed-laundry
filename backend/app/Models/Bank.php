<?php

namespace App\Models;

use App\Traits\ModelFilterTrait;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class Bank extends Model
{
    use Notifiable, ModelFilterTrait;

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


    /**
     * @param $query
     * @param array $params
     * @return mixed
     */
    public function scopeSearch($query, array $params = [])
    {
        $query = $this->likeEmpty('name', $params, $query);
        $query = $this->likeEmpty('code', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        $query->where(function ($subQuery) use ($params) {
            $subQuery = $this->universalSearch($params, $subQuery, [ 'name', 'code',]);
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
        $query = $this->whereEmpty('name', $params, $query);
        $query = $this->whereEmpty('code', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }
}
