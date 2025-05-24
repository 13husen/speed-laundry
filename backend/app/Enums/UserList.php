<?php

namespace App\Enums;

class UserList
{
    const TYPE_USER = "user";
    const TYPE_ADMIN = "admin";
    const TYPE_DRIVER = "driver";
    const TYPE_KARYAWAN = "karyawan";

    const STATUS_AKTIF = 1;
    const STATUS_TIDAK_AKTIF =2;
    const STATUS_BLOCK = 3;
    const STATUS_SUSPEND = 4;     

    const ALL_TYPE = [self::TYPE_USER, self::TYPE_ADMIN,self::TYPE_DRIVER,self::TYPE_KARYAWAN];
    const ALL_STATUS = [self::STATUS_AKTIF, self::STATUS_TIDAK_AKTIF,self::STATUS_BLOCK,self::STATUS_SUSPEND];    
}
