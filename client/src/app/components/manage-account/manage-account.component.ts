import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ManageAccountService } from '../../services/manage-account.service';
import { GetUserDto, UpdateUserRequestDto } from '../../models/dto/dtoAll';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-account',
  templateUrl: './manage-account.component.html',
  styleUrls: ['./manage-account.component.css']
})
export class ManageAccountComponent implements OnInit {
  manageAccountForm: FormGroup;
  passwordsMustMatch: boolean = false;

  constructor(private formBuilder: FormBuilder, private manageAccountService: ManageAccountService, private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.manageAccountForm = this.formBuilder.group({
      name: [''],
      email: [''],
      oldPassword: [''],
      newPassword: ['']
    });

    this.managePasswordValidation();

    this.loadUserData();
  }

  loadUserData(): void {
    this.manageAccountService.getUserData().subscribe((data: GetUserDto) => {
      this.manageAccountForm.patchValue({
        name: data.name,
        email: data.email
      });
    });
  }

  managePasswordValidation(): void {
    this.manageAccountForm.valueChanges.subscribe((values) => {
      const oldPassword = values.oldPassword;
      const newPassword = values.newPassword;

      // If one password field is filled, both must be filled
      this.passwordsMustMatch =
        (oldPassword && !newPassword) || (!oldPassword && newPassword);
    });
  }

  onSubmit(): void {
    if (this.manageAccountForm.valid && !this.passwordsMustMatch) {
      const updateRequest: UpdateUserRequestDto = this.manageAccountForm.value;

      this.manageAccountService.updateUserData(updateRequest).subscribe({
        next: () => {
          this.toastrService.success('Account updated successfully!');
          this.manageAccountForm.reset();
          this.passwordsMustMatch = false;
          this.loadUserData();
        },
        error: (error) => {
          // this.toastrService.error('Failed to update account. Please try again.');
          this.toastrService.error(error.error.description);
          console.log(error);
        }
      });
    }
  }
}
