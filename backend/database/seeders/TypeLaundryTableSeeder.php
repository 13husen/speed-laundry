<?php

namespace Database\Seeders;

use App\Models\TypeLaundry;
use Illuminate\Database\Seeder;

class TypeLaundryTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
		$types = [
			["nama" => 'Reguler (3 hari)','count_time'=>3,'count_type'=>1,"deskripsi" => 'Pengerjaan waktu cucian laundry tipe Biasa atau Reguler dengan waktu 3 hari','fee' => 0, 'status' => 1],
			["nama" => 'Express (1 hari)','count_time'=>1,'count_type'=>1,"deskripsi" => 'Pengerjaan waktu cucian laundry tipe Express atau Cepat dengan waktu 1 hari','fee' => 3000, 'status' => 1],
		];
        foreach ($types as $type) {
			TypeLaundry::updateOrCreate($type);
        }
    }
}
