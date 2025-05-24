<?php

namespace Database\Seeders;

use App\Models\CategorizeClothe;
use Illuminate\Database\Seeder;

class CategorizeClotheTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
		$clothes = [
			["clothe" => 'Handuk',"description" => 'Biaya handuk per pcs','fee' => 5000, 'status' => 1],
			["clothe" => 'Sprei Tebal',"description" => 'Biaya sprei tebal per pcs','fee' => 10000, 'status' => 1],
			["clothe" => 'Sprei Tipis',"description" => 'Biaya sprei tipis per pcs','fee' => 8000, 'status' => 1],
			["clothe" => 'Jas',"description" => 'Biaya Jas per pcs','fee' => 15000, 'status' => 1],
			["clothe" => 'Blazer',"description" => 'Biaya blazer per pcs','fee' => 12000, 'status' => 1],
			["clothe" => 'Selimut',"description" => 'Biaya selimut per pcs','fee' => 9000, 'status' => 1],
			["clothe" => 'Bantal',"description" => 'Biaya bantal per pcs','fee' => 7000, 'status' => 1],
			["clothe" => 'Guling',"description" => 'Biaya guling per pcs','fee' => 7000, 'status' => 1],			
			["clothe" => 'Jaket Tebal',"description" => 'Biaya jaket tebal per pcs','fee' => 6000, 'status' => 1],
			["clothe" => 'Jaket Kulit',"description" => 'Biaya jaket kulit per pcs','fee' => 13000, 'status' => 1],						
		];
        foreach ($clothes as $clothe) {
			CategorizeClothe::updateOrCreate($clothe);
        }
    }
}
