<?php

namespace App\Enums;

class PaymentList
{
    const METHOD_CASH = 1;
    const METHOD_BANK = 2; //usually for topup
    const METHOD_WALLET = 3;

    const ACTION_TOPUP = 1;
    const ACTION_LAUNDRY = 2;

    const STATUS_PENDING = 1;
    const STATUS_CONFIRM_CHECKING = 2;
    const STATUS_FAILED = 3;
    const STATUS_SUCCESS = 4;



    const ALL_STATUS = [self::STATUS_PENDING, self::STATUS_CONFIRM, self::STATUS_SUCCESS, self::STATUS_FAILED,self::STATUS_CONFIRM_CHECKING];
    const ALL_METHOD = [self::METHOD_BANK, self::METHOD_CASH, self::METHOD_WALLET];
    const ALL_METHOD_DETAIL = [
        ["name" => "bank", "id" => self::METHOD_BANK], 
        ["name" => "cash", "id" => self::METHOD_CASH], 
        ["name" => "wallet", "id" => self::METHOD_WALLET]
    ];
    const ALL_ACTION = [self::ACTION_LAUNDRY, self::ACTION_TOPUP];
}