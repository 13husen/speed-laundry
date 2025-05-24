<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTypeLaundriesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('type_laundries', function (Blueprint $table) {
            $table->id();
            $table->string("nama");
            $table->integer('count_time')->default(0);
            $table->integer('count_type')->default(0);
            $table->text("deskripsi");
            $table->double('fee',11, 2);
            $table->integer('status')->default(1);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('type_laundries');
    }
}
