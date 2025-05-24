<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Traits\FileTrait;
use Illuminate\Database\Seeder;

class CategoryTableSeeder extends Seeder
{
    use FileTrait;

    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
		$categories = [
			["name" => 'Setrika', "detail" => "hanya gosok","description" => 'Hanya Gosok','fee' => 5000, 'status' => 1],
			["name" => 'Cuci + Setrika', "detail" => "Cuci dan setrika","description" => 'Hanya Cuci','fee' => 7000, 'status' => 1],
		];
        foreach ($categories as $category) {
			Category::updateOrCreate($category);
        }
    }
}
