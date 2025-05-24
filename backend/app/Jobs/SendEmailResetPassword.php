<?php

namespace App\Jobs;

use App\Models\JobException;
use App\Models\PasswordReset;
use Exception;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\SerializesModels;

class SendEmailResetPassword extends Mailable implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    protected $registeredId;

    /**
     * Create a new message instance.
     *
     * @param $registeredId
     */
    public function __construct($registeredId)
    {
        $this->registeredId = $registeredId;
    }

    public function build()
    {
        $registeredUser = PasswordReset::where('id', $this->registeredId)->first();
        if (!$registeredUser) {
            return false;
        }

        return $this->from(config('mail.from.address'))
            ->subject('Laundry App Reset Password')
            ->view('reset-password')
            ->with([
                'link' => 'http://localhost:8000/users/reset-password/?email=' . $registeredUser->email .
                    '&token=' . $registeredUser->token.'&type=' . $registeredUser->type,
            ]);
    }

    public function failed(Exception $ex)
    {
        $recipients = [];
    }

    /**
     * Get the tags that should be assigned to the job.
     *
     * @return array
     */
    public function tags()
    {
        return ['SendEmailResetPassword',  'registeredId:' . ($this->registeredId ?? '')];
    }
}
