<?php

namespace App\Models;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;
use App\Notifications\PasswordResetNotification;
use App\Traits\ModelFilterTrait;

class User extends Authenticatable implements MustVerifyEmail
{
    use HasApiTokens, HasFactory, Notifiable,ModelFilterTrait;

    protected $guard_name = 'api';
    protected $guarded = [];
    protected $with = ['userDetail'];
    /**
     * The attributes that should be hidden for serialization.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array<string, string>
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function userDetail()
    {
        return $this->belongsTo(UserDetail::class, 'user_detail_id');
    }


    public function sendPasswordResetNotification($token)
    {
        $this->notify(new PasswordResetNotification($token));
    }


    /**
     * @param $query
     * @param array $params
     * @return mixed
     */
    public function scopeSearch($query, array $params = [])
    {
        $query = $this->likeEmpty('name', $params, $query);
        $query = $this->likeEmpty('email', $params, $query);
        $query = $this->likeEmpty('status', $params, $query);
        $query = $this->likeEmpty('type', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        $query->where(function ($subQuery) use ($params) {
            $subQuery = $this->universalSearch($params, $subQuery, [ 'name', 'email','status']);
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
        $query = $this->whereEmpty('email', $params, $query);
        $query = $this->whereEmpty('status', $params, $query);
        $query = $this->whereEmpty('type', $params, $query);

        $query = $this->generateSort($params, $query);
        $query = $this->generateDateRange($params, $query);

        return $query;
    }
    
}
