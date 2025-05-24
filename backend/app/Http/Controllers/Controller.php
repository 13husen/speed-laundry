<?php

namespace App\Http\Controllers;

use App\Enums\UserList;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Routing\Controller as BaseController;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    private $msgSuccess = "Aksi berhasil !";
    private $msgFailed = "Aksi gagal !";
    private $msgUnauthorized = "Akses ditolak !";
    private $msgError = "Error Internal!";
    private $msgForbidden = "Akses Terlarang!";
    private $msgOutofservice = "Layanan sedang Tidak berjalan !";

    private function responseJson(int $statusCode, String $message, array $data)
    {
        return response()->json(
            [
                'message' => $message,
                'data' => $data,
                'code' => $statusCode
            ],
            $statusCode
        );
    }

    /**
     * @param String $message
     * @param array $data
     * @return JsonResponse
     */
    public function responseSuccess(array $data = [], String $message = null)
    {
        return $this->responseJson(200, $message ?? $this->msgSuccess, $data);
    }

    /**
     * @return JsonResponse
     */
    public function responseUnauthorized()
    {
        return $this->responseJson(401, $this->msgUnauthorized, []);
    }

    /**
     * @param null $message
     * @return JsonResponse
     */
    public function responseForbidden($message = null)
    {
        return $this->responseJson(403, $message ?? $this->msgForbidden, []);
    }

    /**
     * @param String $message
     * @param array $data
     * @return JsonResponse
     */
    public function responseFailed(array $data = [], String $message = null)
    {
        return $this->responseJson(400, $message ?? $this->msgFailed, $data);
    }

    /**
     * @param String $message
     * @param array $data
     * @return JsonResponse
     */
    public function responseOutOfService(String $message = null, array $data = [])
    {
        return $this->responseJson(503, $message ?? $this->msgOutofservice, []);
    }

    /**
     * @param String $message
     * @return JsonResponse
     */
    public function responseError(String $message = null)
    {
        return $this->responseJson(500, $message ?? $this->msgError, []);
    }

    public function isOwner($recordOwnerId)
    {
        $currentUser = auth()->user();
        if ($currentUser->id == $recordOwnerId) {
            return true;
        }

        return false;
    }

    public function isAdmin()
    {
        $currentUser = auth()->user();

        if ($currentUser->type == UserList::TYPE_ADMIN) {
            return true;
        }

        return false;
    }
}
