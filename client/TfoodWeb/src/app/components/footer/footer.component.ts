import { Component, HostListener } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  isVisible = false;

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    const screenHeight = window.innerHeight;
    const mouseY = event.clientY;

    // Kiểm tra nếu con trỏ chuột ở cách cuối trang khoảng 4-5 rem (khoảng 64-80px)
    this.isVisible = screenHeight - mouseY <= 80;
  }
}
