import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { ImageCroppedEvent } from 'ngx-image-cropper';
@Component({
  selector: 'app-avatar-upload-dialog',
  templateUrl: './avatar-upload-dialog.component.html',
  styleUrls: ['./avatar-upload-dialog.component.scss']
})
export class AvatarUploadDialogComponent {
  imageChangedEvent: any = '';
  croppedImage: any = '';

  constructor(
    public dialogRef: MatDialogRef<AvatarUploadDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient
  ) { }

  // Triggered when a file is selected
  onFileSelected(event: Event) {
    this.imageChangedEvent = event;
  }

  // Triggered when the image is cropped
  onImageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
  }

  // Save the avatar by sending the cropped image to the server
  saveAvatar() {
    if (this.croppedImage) {
      // Send the cropped image to the server via an API call
      const formData = new FormData();
      const fileBlob = this.dataURLtoBlob(this.croppedImage); // Convert base64 to Blob
      formData.append('avatar', fileBlob, 'avatar.png');

      // // Replace 'your-api-endpoint' with the actual URL
      // this.http.post('your-api-endpoint', formData).subscribe(
      //   (response) => {
      //     this.dialogRef.close(response); // Close dialog and pass back the server response
      //   },
      //   (error) => {
      //     console.error("Error uploading avatar:", error);
      //   }
      // );
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
