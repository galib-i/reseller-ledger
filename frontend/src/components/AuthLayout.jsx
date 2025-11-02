import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';

export function AuthLayout({
  title,
  fields = [],
  onSubmit,
  submitButtonText = 'Submit',
  showRememberMe = false,
  rememberMe,
  onRememberMeChange,
  showForgotPassword = false,
  showGoogleAuth = false,
  showTerms = false,
  footerText,
  footerLinkText,
  footerLinkTo,
}) {
  return (
    <div className="relative flex min-h-screen flex-col items-center justify-center bg-zinc-950 p-4">
      {/* Close button */}
      <Link
        to="/"
        className="absolute top-8 right-8 text-zinc-400 transition-colors hover:text-zinc-100"
        aria-label="Close"
      >
        <svg
          width="48"
          height="48"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="1"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <line x1="18" y1="6" x2="6" y2="18" />
          <line x1="6" y1="6" x2="18" y2="18" />
        </svg>
      </Link>

      {/* Main container */}
      <div className="w-full max-w-sm space-y-4 p-12">
        {/* Book icon */}
        <div className="flex justify-center">
          <svg
            width="48"
            height="48"
            viewBox="0 0 128 128"
            xmlns="http://www.w3.org/2000/svg"
            className="text-zinc-300"
          >
            <path
              fill="currentColor"
              d="M66.647 77.94 128 49.733v5.14L66.491 83.174 12.303 53.876l-.016.047a4.704 4.704 0 0 0-5.934 2.991c-1.371 3.863 3.084 5.997 3.598 6.23l.156.078 55.154 29.812L128 64.171v5.156L65.089 98.251 10.917 68.969l-.016.031a4.634 4.634 0 0 0-3.583.28 4.596 4.596 0 0 0-2.352 2.71c-1.402 3.987 3.38 6.137 3.582 6.23l.171.078 55.154 29.812L128 77.785v5.171l-64.297 30.373L6.571 82.457C3.456 81.055-1.684 76.803.543 70.479a9.208 9.208 0 0 1 4.283-5.156c-2.43-2.134-4.455-5.514-2.897-9.922a9.265 9.265 0 0 1 4.283-5.171c-2.43-2.118-4.439-5.514-2.882-9.906.763-2.321 2.43-4.252 4.657-5.389 2.056-1.028 62.35-20.264 62.35-20.264L128 40.06 66.569 66.835l-52.88-27.99c-.016 0-.031-.016-.047-.016-2.461-.794-5.078.561-5.903 3.006-1.355 3.863 3.1 5.997 3.598 6.23l.171.078L66.647 77.94z"
            />
          </svg>
        </div>

        <p className="text-center text-2xl font-bold text-white">{title}</p>

        <form onSubmit={onSubmit} className="space-y-4">
          {/* Dynamic form fields */}
          <div className="space-y-2">
            {fields.map((field) => (
              <div key={field.id}>
                <Input
                  id={field.id}
                  type={field.type}
                  placeholder={field.placeholder}
                  value={field.value}
                  onChange={field.onChange}
                  className="border-zinc-800 bg-zinc-900 text-white placeholder:text-zinc-500 focus-visible:ring-zinc-400"
                  required={field.required !== false}
                />
                {field.error && (
                  <p className="mt-1 text-center text-sm text-red-600">{field.error}</p>
                )}
              </div>
            ))}
          </div>

          {/* Remember me and Forgot password*/}
          {(showRememberMe || showForgotPassword) && (
            <div className="flex items-center justify-between pb-4">
              {showRememberMe && (
                <div className="flex items-center">
                  <input
                    id="rememberMe"
                    type="checkbox"
                    checked={rememberMe}
                    onChange={() => onRememberMeChange?.(!rememberMe)}
                    className="h-3 w-3 cursor-pointer rounded accent-zinc-800"
                  />
                  <label htmlFor="rememberMe" className="ml-2 cursor-pointer text-sm text-zinc-500">
                    Remember me
                  </label>
                </div>
              )}
              {showForgotPassword && (
                <Link to="#" className="text-sm text-zinc-500 hover:underline">
                  Forgot password?
                </Link>
              )}
            </div>
          )}

          {/* Terms and conditions */}
          {showTerms && (
            <p className="pb-2 text-center text-xs text-zinc-500">
              By signing up, you agree to our{' '}
              <Link to="/terms" className="font-semibold hover:underline">
                Terms of Service
              </Link>{' '}
              and{' '}
              <Link to="/privacy" className="font-semibold hover:underline">
                Privacy Policy
              </Link>
            </p>
          )}

          {/* Submit button */}
          <Button
            type="submit"
            variant="default"
            className="w-full bg-zinc-300 transition-shadow hover:shadow-[0_0_80px_rgba(255,255,255,0.7)]"
          >
            {submitButtonText}
          </Button>
        </form>

        {showGoogleAuth && (
          <div className="-mt-2 space-y-2">
            <p className="text-center text-xs font-bold text-zinc-500">OR</p>

            {/* Continue with Google button */}
            <Button
              variant="outline"
              className="flex w-full items-center justify-center gap-2 border-zinc-700 text-zinc-300 hover:bg-zinc-800"
            >
              <img
                src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
                alt="Google logo"
                className="h-5 w-5"
              />
              Continue with Google
            </Button>
          </div>
        )}

        {/* Footer*/}
        {footerText && footerLinkText && footerLinkTo && (
          <p className="text-center text-sm text-zinc-400">
            {footerText}{' '}
            <Link to={footerLinkTo} className="font-bold hover:underline">
              {footerLinkText}
            </Link>
          </p>
        )}
      </div>
    </div>
  );
}

AuthLayout.displayName = 'AuthLayout';
