<?php

namespace App\Models;

use App\Traits\ModelFilterTrait;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;

class TypeLaundry extends Model
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
        $query = $this->likeEmpty('nama', $params, $query);
        $query = $this->likeEmpty('deskripsi', $params, $query);
        $query = $this->likeEmpty('fee', $params, $query);
        $query = $this->likeEmpty('status', $params, $query);

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
        $query = $this->whereEmpty('nama', $params, $query);
        $query = $this->whereEmpty('deskripsi', $params, $query);
        $query = $this->whereEmpty('fee', $params, $query);
        $query = $this->whereEmpty('status', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }
}
