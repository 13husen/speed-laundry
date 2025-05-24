<?php

namespace App\Traits;

use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Storage;
use Intervention\Image\Facades\Image;
use Spatie\ImageOptimizer\OptimizerChainFactory;

trait FileTrait
{

    public function deleteFile($fileName)
    {
        $disk = Storage::disk('local');

        if ($disk->has('/upload/'. $fileName)) {
            return $disk->delete('/upload/'. $fileName);
        }

        return false;
    }

    /**
     * Upload image
     *
     * @param string $base64EncodeImage
     * @param string $path Path to upload
     * @param integer $width
     * @param integer $height
     * @param bool $remainAspectRatio
     * @return mixed Return file path if success else false if fail
     */
    public function uploadImage($base64EncodeImage, $width = 0, $height = 0, $remainAspectRatio = false)
    {
        $imageData = base64_decode($base64EncodeImage);
        $imageFileName = mt_rand(1000, 9999999) . time() . '.png';

        $image = Image::make($imageData);
        $disk = Storage::disk('local');
        if ($width != 0 || $height != 0) {
            $image->resize($width, $height, function ($constraint) use ($remainAspectRatio) {
                if ($remainAspectRatio) {
                    $constraint->aspectRatio();
                }
            });
        }

        $image->save(storage_path('app/public/' . $imageFileName));
        $optimizerChain = OptimizerChainFactory::create();
        $optimizerChain->optimize(storage_path('app/public/' . $imageFileName));
        $disk->put('/upload' . '/' . $imageFileName, $image);

        return $imageFileName;
    }


    public function localCleanDirectory($directory)
    {
        $files = Storage::disk('local')->files($directory);

        foreach ($files as $file) {
            Storage::disk('local')->delete($file);
        }
    }

    public function localDeleteFile($path, $fileName)
    {
        if (Storage::disk('local')->exists($path . $fileName)) {
            return Storage::disk('local')->delete($path . $fileName);
        }

        return false;
    }

    public function localUploadFile($path, $file)
    {
        $filename = mt_rand(1000, 9999) . time() . '.' . $file->getClientOriginalExtension();
        Storage::disk('local')->putFileAs($path, $file, $filename);
        return $filename;
    }
}
