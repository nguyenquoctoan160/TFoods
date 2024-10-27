import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NgxImageCompressService } from 'ngx-image-compress';
import { ImageCroppedEvent } from 'ngx-image-cropper';
import { AvatarUploadServiceService } from 'src/app/services/avatar-upload-service.service';
@Component({
  selector: 'app-avatar-upload-dialog',
  templateUrl: './avatar-upload-dialog.component.html',
  styleUrls: ['./avatar-upload-dialog.component.scss'],
  providers: [NgxImageCompressService]
})
export class AvatarUploadDialogComponent {
  imageChangedEvent: any = '';
  croppedImage: any = '';
  isLoading: boolean = false;
  constructor(
    public dialogRef: MatDialogRef<AvatarUploadDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,
    private imageCompress: NgxImageCompressService,
    private avatarUploadService: AvatarUploadServiceService
  ) { }

  // Triggered when a file is selected
  onFileSelected(event: Event) {
    this.imageChangedEvent = event;
  }

  // Triggered when the image is cropped
  async onImageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;

    // Check if the image is larger than 100MB (100 * 1024 * 1024 bytes)
    const imageBlob = this.dataURLtoBlob(this.croppedImage);
    if (imageBlob.size > 104_857_600) {
      // Reduce the quality to fit within the 100MB limit
      this.croppedImage = await this.compressImage(this.croppedImage);
    }
  }
  // Compress image quality if it exceeds 100MB
  private async compressImage(image: string): Promise<string> {
    let compressedImage = image;
    let quality = 100;
    let imageBlob = this.dataURLtoBlob(image);

    // Loop to reduce quality in steps until the image is below 100MB
    while (imageBlob.size > 104_857_600 && quality > 10) {
      quality -= 10;
      compressedImage = await this.imageCompress.compressFile(image, -1, quality, quality);
      imageBlob = this.dataURLtoBlob(compressedImage);
    }
    return compressedImage;
  }
  // Save the avatar by sending the cropped image to the server
  saveAvatar() {
    if (this.croppedImage) {
      this.isLoading = true;
      const fileBlob = this.dataURLtoBlob(this.croppedImage);

      this.avatarUploadService.uploadAvatar(fileBlob).subscribe(
        (response) => {
          this.isLoading = false;
          this.dialogRef.close(response);
          window.location.reload();
        },
        (error) => {
          console.error(error);
          this.isLoading = false;
        }
      );
    } else {
      this.dialogRef.close(null);
    }
  }

  // Helper function to convert a base64 string to a Blob
  private dataURLtoBlob(dataUrl: string): Blob {
    const byteString = atob(dataUrl.split(',')[1]);
    const mimeString = dataUrl.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: mimeString });
  }
}
