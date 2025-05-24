<?php

namespace App\Enums;

class TransactionList
{
    const STATUS_PROSES = 1; // driver
    const STATUS_ANTAR = 2; // driver
    const STATUS_PENGECEKAN = 3; // driver
    const STATUS_PENDING_PAYMENT = 4; // admin
    const STATUS_CUCI = 6; // karyawan
    const STATUS_ANTARPULANG = 7; // driver
    const STATUS_SELESAI = 8; 
    const STATUS_CANCEL = 9;
    const STATUS_EXPIRED = 11;

    const ACTION_TOPUP = 1;
    const ACTION_LAUNDRY = 2;

    const DO_TOLAK = 1;
    const DO_TERIMA = 2;


    const ALL_STATUS = [self::STATUS_PROSES, self::STATUS_ANTAR, self::STATUS_PENGECEKAN, self::STATUS_PENDING_PAYMENT,self::STATUS_CUCI,self::STATUS_EXPIRED,self::STATUS_ANTARPULANG,self::STATUS_SELESAI,self::STATUS_CANCEL];

    const ALL_ACTION = [self::ACTION_TOPUP, self::ACTION_LAUNDRY];    
    const ALL_DO = [self::DO_TERIMA, self::DO_TOLAK];    
}