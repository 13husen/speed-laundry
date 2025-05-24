<?php

namespace App\Enums;

class RoleList
{
    const ROLE_ADMIN = 'admin';
    const ROLE_USER = 'user';

    const ALL_ROLES = [self::ROLE_ADMIN, self::ROLE_USER];
}