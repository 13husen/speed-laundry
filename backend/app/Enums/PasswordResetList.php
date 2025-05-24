<?php

namespace App\Enums;

class PasswordResetList
{
    const STATUS_PENDING = 1;
    const STATUS_USED = 2;

    const ALL_STATUS = [self::STATUS_PENDING, self::STATUS_USED,];
}