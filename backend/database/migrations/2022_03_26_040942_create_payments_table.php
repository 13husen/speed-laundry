<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePaymentsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('payments', function (Blueprint $table) {
            $table->id();
            $table->integer('payment_type')->nullable()->comment("1:cash,2:bank,3:wallet");
            $table->double('shipping_fee',11, 2)->nullable();
            $table->double('total_fee',11, 2)->nullable();
            $table->double('discount_fee',11, 2)->nullable();
            $table->double('nominal',11, 2)->nullable();
            $table->double('admin_fee',11, 2)->nullable();
            $table->integer('confirm_count')->default(0);
            $table->date('confirm_payment_date')->nullable();
            $table->integer('status')->default(1)->comment("0:pending/created,1:confirmation,2:confirm checking, 3:error/failed, 4: success");
            $table->text('detail')->nullable();            
            $table->timestamps();
            $table->softDeletes();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('payments');
    }
}
