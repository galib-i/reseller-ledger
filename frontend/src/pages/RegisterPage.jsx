import { AuthLayout } from '@/components/AuthLayout';
import { useState } from 'react';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errors, setErrors] = useState({});

  const fields = [
    {
      id: 'email',
      type: 'email',
      placeholder: 'Email',
      value: email,
      onChange: (e) => setEmail(e.target.value),
      error: errors.email,
    },
    {
      id: 'password',
      type: 'password',
      placeholder: 'Password',
      value: password,
      onChange: (e) => setPassword(e.target.value),
      error: errors.password,
    },
    {
      id: 'confirmPassword',
      type: 'password',
      placeholder: 'Confirm Password',
      value: confirmPassword,
      onChange: (e) => setConfirmPassword(e.target.value),
      error: errors.confirmPassword,
    },
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log({ email, password, confirmPassword });
    // Handle register logic here
  };

  return (
    <AuthLayout
      title="Create your Ledger"
      fields={fields}
      onSubmit={handleSubmit}
      submitButtonText="Sign up"
      showTerms={true}
      showGoogleAuth={true}
      footerText="Already have an account?"
      footerLinkText="Sign in"
      footerLinkTo="/login"
    />
  );
}
