<?php

namespace App\Enums;

class GeneralList
{
    const STATUS_ACTIVE = 1;
    const STATUS_INACTIVE = 2;

    const COUNT_HARI = 1;
    const COUNT_JAM = 2;    

    const ALL_STATUS = [self::STATUS_ACTIVE, self::STATUS_INACTIVE];
    const ALL_COUNT = [self::COUNT_HARI, self::COUNT_JAM];
}