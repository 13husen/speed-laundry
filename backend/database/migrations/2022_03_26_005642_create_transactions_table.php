
<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTransactionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('transactions', function (Blueprint $table) {
            $table->id();
            $table->integer('user_id');
            $table->integer('payment_id');
            $table->string("invoice");
            $table->integer('status')->default(1)->comment("1:di proses,2:di antar,3: proses pengecekan, 4:pending payment,5:di cuci,6: expired,7:diantar pulang,8:selesai");
            $table->integer('courier_id')->nullable();
            $table->integer('type_id')->nullable()->comment('type waktu pengerjaan,contoh reguler, ekspress');            
            $table->text("shipment_fix_name")->nullable();
            $table->text("shipment_fix_phone")->nullable();
            $table->text("shipment_fix_address")->nullable();
            $table->integer("type")->nullable()->comment("topup or laundry transaction")->nullable();          
            $table->boolean("is_handled")->default(0);  
            $table->integer('handled_by')->nullable();
            $table->text("note")->nullable();
            $table->dateTime('job_done_at')->nullable()->comment('waktu laundry selesai');            
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
        Schema::dropIfExists('transactions');
    }
}
