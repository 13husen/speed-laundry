<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        $this->call(BankTableSeeder::class);
        $this->call(CategorizeClotheTableSeeder::class);
        $this->call(CategoryTableSeeder::class);
        $this->call(TypeLaundryTableSeeder::class);
        // \App\Models\User::factory(10)->create();
    }
}
